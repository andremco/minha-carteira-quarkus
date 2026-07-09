package org.finance.services;

import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.finance.configs.ApiConfigProperty;
import org.finance.exceptions.NegocioException;
import org.finance.mappers.MoedaMapper;
import org.finance.models.data.mariadb.entities.Moeda;
import org.finance.models.request.moeda.EditarMoedaRequest;
import org.finance.models.request.moeda.SalvarMoedaRequest;
import org.finance.models.response.Paginado;
import org.finance.models.response.coin.CoinResponse;
import org.finance.models.response.moeda.DetalharMoedaResponse;
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

    public DetalharMoedaResponse detalharMoeda(Integer id){
        Moeda moeda = moedaRepository.findById(id.longValue());
        if (moeda == null || moeda.getDataRegistroRemocao() != null)
            throw new NegocioException(apiConfigProperty.getRegistroNaoEncontrado());

        var todasMoedas = moedaRepository.listAll();

        var somaTodasNotasMoedas = todasMoedas.stream()
                .mapToInt(Moeda::getNota)
                .sum();

        var coin = obterCotacao(moeda.getCodigo());

        var aportes = dashboardService.obterAportesTotal(null, null);
        var totalCarteira = aportes.getTotalMoedas().doubleValue();

        var valorTotalCompras = aporteService.comprasRealizadas(moeda.getAportes());
        var valorTotalVendas = aporteService.vendasRealizadas(moeda.getAportes());
        var valorTotalAtivo = valorTotalCompras-valorTotalVendas;
        var valorTotalAtivoAtual = aporteService.calcularValorTotalAtivoAtual(moeda.getAportes(), coin.getPrecoDinamico());
        var carteiraIdealPorcento = calculosCarteira.calcularCarteiraIdealQuociente(moeda.getNota(), somaTodasNotasMoedas);
        var carteiraTenhoPorcento = calculosCarteira.calcularCarteiraTenhoQuociente(valorTotalAtivo, totalCarteira);
        var quantoQueroTotal = calculosCarteira.calcularQuantoQuero(carteiraIdealPorcento, totalCarteira);
        var quantoFaltaTotal = calculosCarteira.calcularQuantoFalta(quantoQueroTotal, valorTotalAtivo);
        var quantidadeQueTenho = aporteService.calcularQuantidadeCompras(moeda.getAportes());
        var quantidadeQueFaltaTotal = (double) Math.round(calculosCarteira.calcularQuantidadeQueFalta(quantoFaltaTotal, coin.getPrecoDinamico()));
        var comprarOuAguardar = calculosCarteira.informarComprarOuAguardar(carteiraIdealPorcento, carteiraTenhoPorcento);
        var lucroOuPerda = calculosCarteira.calcularLucroOuPerda(valorTotalAtivo, valorTotalAtivoAtual);

        return moedaMapper.toDetalharMoedaResponse(moeda, coin.getPrecoDinamico(),
                (carteiraIdealPorcento*100), (carteiraTenhoPorcento*100),
                valorTotalAtivo, valorTotalAtivoAtual,
                valorTotalCompras, valorTotalVendas,
                quantoQueroTotal, quantoFaltaTotal,
                quantidadeQueTenho, quantidadeQueFaltaTotal,
                comprarOuAguardar, lucroOuPerda);
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
        var totalCarteira = aportes.getTotalMoedas().doubleValue();

        moedas.forEach(moeda -> {
            var coin = obterCotacao(moeda.getCodigo());

            var quantidadeQueTenho = aporteService.calcularQuantidadeCompras(moeda.getAportes());
            var valorTotalAtivoAtual = aporteService.calcularValorTotalAtivoAtual(moeda.getAportes(), coin.getPrecoDinamico());
            var valorTotalAtivo = aporteService.calcularValorTotalAtivo(moeda.getAportes());

            var carteiraTenhoPorcento = calculosCarteira.calcularCarteiraTenhoQuociente(valorTotalAtivo, totalCarteira);
            var carteiraIdealPorcento = calculosCarteira.calcularCarteiraIdealQuociente(moeda.getNota(), somaTodasNotasMoedas);

            var comprarOuAguardar = calculosCarteira.informarComprarOuAguardar(carteiraIdealPorcento, carteiraTenhoPorcento);
            var lucroOuPerda = calculosCarteira.calcularLucroOuPerda(valorTotalAtivo, valorTotalAtivoAtual);

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
