package org.finance.services;

import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.finance.configs.ApiConfigProperty;
import org.finance.exceptions.NegocioException;
import org.finance.mappers.CoinMapper;
import org.finance.mappers.MoedaMapper;
import org.finance.models.data.mariadb.entities.Moeda;
import org.finance.models.request.moeda.EditarMoedaRequest;
import org.finance.models.request.moeda.SalvarMoedaRequest;
import org.finance.models.response.Paginado;
import org.finance.models.response.coin.CoinResponse;
import org.finance.models.response.moeda.MoedaResponse;
import org.finance.repositories.mariadb.MoedaRepository;
import org.finance.utils.CalculosCarteira;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class MoedaService {
    @Inject
    MoedaRepository moedaRepository;
    @Inject
    MoedaMapper moedaMapper;
    @Inject
    ApiConfigProperty apiConfigProperty;
    @Inject
    CoinService coinService;
    @Inject
    DashboardService dashboardService;
    @Inject
    CalculosCarteira calculosCarteira;
    @Inject
    AporteService aporteService;

    public MoedaResponse salvar(SalvarMoedaRequest request) {
        var codigo = request.getCodigo().toUpperCase();
        var existePorNome = moedaRepository.find("dataRegistroRemocao is null and nome = :nome",
                Parameters.with("nome", request.getNome())).count();
        var existePorCodigo = moedaRepository.find("dataRegistroRemocao is null and codigo = :codigo",
                Parameters.with("codigo", codigo)).count();

        if(existePorNome != 0 || existePorCodigo != 0)
            throw new NegocioException(apiConfigProperty.getRegistroJaExiste());

        var moeda = moedaMapper.toMoeda(request);
        moedaRepository.persist(moeda);

        return moedaMapper.toMoedaResponse(moeda);
    }

    public MoedaResponse editar(EditarMoedaRequest request) throws NegocioException {
        Moeda moeda = moedaRepository.findById(request.getId().longValue());
        if (moeda == null || moeda.getDataRegistroRemocao() != null)
            throw new NegocioException(apiConfigProperty.getRegistroNaoEncontrado());

        String codigo = request.getCodigo() != null ? request.getCodigo().toUpperCase() : null;
        Moeda findPorNome = null;
        Moeda findPorCodigo = null;

        if (request.getNome() != null)
            findPorNome = moedaRepository.find("dataRegistroRemocao is null and nome = :nome",
                    Parameters.with("nome", request.getNome())).firstResult();
        if (codigo != null)
            findPorCodigo = moedaRepository.find("dataRegistroRemocao is null and codigo = :codigo",
                    Parameters.with("codigo", codigo)).firstResult();

        if (findPorNome != null && !Objects.equals(findPorNome.getId(), request.getId()) ||
                findPorCodigo != null && !Objects.equals(findPorCodigo.getId(), request.getId()))
            throw new NegocioException(apiConfigProperty.getRegistroJaExiste());

        if (request.getNome() != null)
            moeda.setNome(request.getNome());
        if (codigo != null)
            moeda.setCodigo(codigo);
        if (request.getNota() != null)
            moeda.setNota(request.getNota());

        moeda.setDataRegistroEdicao(LocalDateTime.now());
        moedaRepository.persist(moeda);

        return moedaMapper.toMoedaResponse(moeda);
    }

    public MoedaResponse detalharMoeda(Integer id){
        Moeda moeda = moedaRepository.findById(id.longValue());
        if (moeda == null || moeda.getDataRegistroRemocao() != null)
            throw new NegocioException(apiConfigProperty.getRegistroNaoEncontrado());

        return moedaMapper.toMoedaResponse(moeda);
    }

    public void excluir(Integer id) throws NegocioException {
        Moeda moeda = moedaRepository.findById(id.longValue());
        if (moeda == null || moeda.getDataRegistroRemocao() != null)
            throw new NegocioException(apiConfigProperty.getRegistroNaoEncontrado());

        moeda.setDataRegistroRemocao(LocalDateTime.now());
        moedaRepository.persist(moeda);
    }

    public Paginado<MoedaResponse> filtrarMoedas(String descricaoMoeda, Integer pagina, Integer tamanho){
        long totalMoedas = total(descricaoMoeda);
        var moedas = moedaRepository.findMoedasPaged(descricaoMoeda, pagina, tamanho);
        List<MoedaResponse> response = new ArrayList<>();

        var todasMoedas = moedaRepository.listAll();

        var somaTodasNotasMoedas = todasMoedas.stream()
                .mapToInt(Moeda::getNota)
                .sum();
        var aportes = dashboardService.obterAportesTotal(null, null);
        var totalCarteira = aportes.getTotalTitulos().doubleValue();

        moedas.forEach(moeda -> {
            var coin = coinService.obter(moeda.getCodigo());
            if (coin == null)
                throw new NegocioException(apiConfigProperty.getRegistroNaoEncontrado());

            var valorTotalAtivoAtual = aporteService.calcularValorTotalAtivoAtual(moeda.getAportes(), coin.getPrecoDinamico());
            var valorTotalAtivo = aporteService.calcularValorTotalAtivo(moeda.getAportes());

            var carteiraTenhoPorcento = calculosCarteira.calcularCarteiraTenhoQuociente(valorTotalAtivo, totalCarteira);
            var carteiraIdealPorcento = calculosCarteira.calcularCarteiraIdealQuociente(moeda.getNota(), somaTodasNotasMoedas);

            var lucroOuPerda = calculosCarteira.calcularLucroOuPerda(valorTotalAtivo, valorTotalAtivoAtual);
            var comprarOuAguardar = calculosCarteira.informarComprarOuAguardar(carteiraIdealPorcento, carteiraTenhoPorcento);
            var quantidadeQueTenho = aporteService.calcularQuantidadeCompras(moeda.getAportes());

            response.add(moedaMapper.toMoedaResponse(moeda, coin.getPrecoDinamico(), quantidadeQueTenho, valorTotalAtivoAtual, comprarOuAguardar, lucroOuPerda));
        });

        return Paginado.<MoedaResponse>builder()
                .pagina(pagina)
                .tamanho(tamanho)
                .total(totalMoedas)
                .itens(response)
                .build();
    }

    public long total(String descricaoMoeda){
        return moedaRepository.total(descricaoMoeda);
    }

    private CoinResponse obterCotacao(String codigo) {
        var cotacao = coinService.obter(codigo);
        if (cotacao == null)
            throw new NegocioException(apiConfigProperty.getRegistroNaoEncontrado());
        return cotacao;
    }
}
