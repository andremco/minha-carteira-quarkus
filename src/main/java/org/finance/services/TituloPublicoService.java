package org.finance.services;

import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.finance.configs.ApiConfigProperty;
import org.finance.exceptions.NegocioException;
import org.finance.mappers.TituloPublicoMapper;
import org.finance.models.data.mariadb.entities.Acao;
import org.finance.models.data.mariadb.entities.Setor;
import org.finance.models.data.mariadb.entities.TituloPublico;
import org.finance.models.enums.TipoAtivoEnum;
import org.finance.models.request.tituloPublico.EditarTituloPublicoRequest;
import org.finance.models.request.tituloPublico.SalvarTituloPublicoRequest;
import org.finance.models.response.Paginado;
import org.finance.models.response.tituloPublico.DetalharTituloPublicoResponse;
import org.finance.models.response.tituloPublico.TituloPublicoResponse;
import org.finance.repositories.mariadb.AcaoRepository;
import org.finance.repositories.mariadb.DashboardRepository;
import org.finance.repositories.mariadb.SetorRepository;
import org.finance.repositories.mariadb.TituloPublicoRepository;
import org.finance.utils.CalculosCarteira;
import org.finance.utils.Formatter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class TituloPublicoService {
    @Inject
    TituloPublicoRepository tituloPublicoRepository;
    @Inject
    TituloPublicoMapper tituloPublicoMapper;
    @Inject
    ApiConfigProperty apiConfigProperty;
    @Inject
    AporteService aporteService;
    @Inject
    DashboardService dashboardService;
    @Inject
    SetorService setorService;
    @Inject
    CalculosCarteira calculosCarteira;

    public TituloPublicoResponse salvar(SalvarTituloPublicoRequest request){
        var existePorDescricao = tituloPublicoRepository.find("dataRegistroRemocao is null and descricao = :descricao",
                Parameters.with("descricao", request.getDescricao().trim())).count();

        if (existePorDescricao != 0)
            throw new NegocioException(apiConfigProperty.getRegistroJaExiste());

        var setor = setorService.buscarPorId(request.getSetorId());

        if (setor == null)
            throw new NegocioException(apiConfigProperty.getRegistroNaoEncontrado());

        var tituloPublico = tituloPublicoMapper.toTituloPublico(request, setor);
        tituloPublicoRepository.persist(tituloPublico);

        return tituloPublicoMapper.toTituloPublicoResponse(tituloPublico, setor);
    }

    public TituloPublicoResponse editar(EditarTituloPublicoRequest request){
        Setor setor = new Setor();

        if (request.getSetorId() != null)
            setor = setorService.buscarPorId(request.getSetorId());

        if (setor == null)
            throw new NegocioException(apiConfigProperty.getRegistroNaoEncontrado());

        TituloPublico tituloPublico = tituloPublicoRepository.findById(request.getId().longValue());
        if (tituloPublico == null)
            throw new NegocioException(apiConfigProperty.getRegistroNaoEncontrado());

        var findPorDescricao = tituloPublicoRepository.find("dataRegistroRemocao is null and descricao = :descricao",
                Parameters.with("descricao", request.getDescricao())).firstResult();

        if (findPorDescricao != null && !Objects.equals(findPorDescricao.getId(), request.getId()))
            throw new NegocioException(apiConfigProperty.getRegistroJaExiste());

        if (request.getDescricao() != null)
            tituloPublico.setDescricao(request.getDescricao());
        if (request.getSetorId() != null)
            tituloPublico.setSetor(setor);
        if (request.getPrecoInicial() != null)
            tituloPublico.setPrecoInicial(request.getPrecoInicial());
        if (request.getValorRendimento() != null)
            tituloPublico.setValorRendimento(request.getValorRendimento());
        if (request.getNota() != null)
            tituloPublico.setNota(request.getNota());

        tituloPublico.setDataRegistroEdicao(LocalDateTime.now());
        tituloPublicoRepository.persist(tituloPublico);

        return tituloPublicoMapper.toTituloPublicoResponse(tituloPublico, setor);
    }

    public DetalharTituloPublicoResponse detalharTitulo(Integer id){
        TituloPublico tituloPublico = tituloPublicoRepository.findById(id.longValue());
        if (tituloPublico == null)
            throw new NegocioException(apiConfigProperty.getRegistroNaoEncontrado());

        TipoAtivoEnum tipoAtivo = TipoAtivoEnum.TITULO_PUBLICO;
        var setores = setorService.obterSetoresTotalNotas(tipoAtivo);
        var somaTodasNotasTituloPublico = calculosCarteira.somarNotasPorSetores(setores);

        var aportes = dashboardService.obterAportesTotal(null, null);
        var totalCarteira = aportes.getTotalTitulos().doubleValue();

        var valorTotalCompras = aporteService.comprasRealizadas(tituloPublico.getAportes());
        var valorTotalVendas = aporteService.vendasRealizadas(tituloPublico.getAportes());
        var precoMedio = calculosCarteira.calcularPrecoMedioAportes(tituloPublico.getAportes());
        var valorTotalAtivo = valorTotalCompras-valorTotalVendas;
        if (tituloPublico.getValorRendimento() != null)
            valorTotalAtivo += tituloPublico.getValorRendimento();
        var carteiraIdealPorcento = calculosCarteira.calcularCarteiraIdealQuociente(tituloPublico.getNota(), somaTodasNotasTituloPublico);
        var carteiraTenhoPorcento = calculosCarteira.calcularCarteiraTenhoQuociente(valorTotalAtivo, totalCarteira);
        var quantoQueroTotal = calculosCarteira.calcularQuantoQuero(carteiraIdealPorcento, totalCarteira);
        var quantoFaltaTotal = calculosCarteira.calcularQuantoFalta(quantoQueroTotal, valorTotalAtivo);
        var precoParaCalculoQuantoFalta = precoMedio == 0 ? tituloPublico.getPrecoInicial() : precoMedio;
        var quantidadeQueFaltaTotal = (int) Math.round(calculosCarteira.calcularQuantidadeQueFalta(quantoFaltaTotal, precoParaCalculoQuantoFalta));
        var lucroOuPerda = tituloPublico.getValorRendimento() != null ? tituloPublico.getValorRendimento() : 0;
        var comprarOuAguardar = calculosCarteira.informarComprarOuAguardar(quantidadeQueFaltaTotal);

        return tituloPublicoMapper.toDetalharTituloPublicoResponse(tituloPublico, Formatter.doubleToReal(precoMedio),
                Formatter.doubleToPorcento(carteiraIdealPorcento), Formatter.doubleToPorcento(carteiraTenhoPorcento),
                Formatter.doubleToReal(valorTotalCompras), Formatter.doubleToReal(valorTotalVendas),
                Formatter.doubleToReal(valorTotalAtivo), Formatter.doubleToReal(quantoQueroTotal),
                Formatter.doubleToReal(quantoFaltaTotal), quantidadeQueFaltaTotal, comprarOuAguardar, Formatter.doubleToReal(lucroOuPerda));
    }

    public void excluir(Integer id) throws NegocioException {
        TituloPublico tituloPublico = tituloPublicoRepository.findById(id.longValue());

        if (tituloPublico == null)
            throw new NegocioException(apiConfigProperty.getRegistroNaoEncontrado());

        var quantidade = AporteService.calcularQuantidadeCompras(tituloPublico.getAportes());
        if (quantidade > 0)
            throw new NegocioException(apiConfigProperty.getTituloNaoPodeSerExcluido());

        tituloPublico.setDataRegistroRemocao(LocalDateTime.now());
        tituloPublicoRepository.persist(tituloPublico);
    }

    public Paginado<TituloPublicoResponse> filtrarTitulos(String descricao, Integer pagina, Integer tamanho){
        long totalTitulos = total(descricao);
        var titulos = tituloPublicoRepository.findTitulosPaged(descricao, pagina, tamanho);
        List<TituloPublicoResponse> response = new ArrayList<>();

        TipoAtivoEnum tipoAtivo = TipoAtivoEnum.TITULO_PUBLICO;
        var setores = setorService.obterSetoresTotalNotas(tipoAtivo);
        var somaTodasNotasTituloPublico = calculosCarteira.somarNotasPorSetores(setores);

        var aportes = dashboardService.obterAportesTotal(null, null);
        var totalCarteira = aportes.getTotalTitulos().doubleValue();

        titulos.forEach(titulo -> {
            var carteiraIdealPorcento = calculosCarteira.calcularCarteiraIdealQuociente(titulo.getNota(), somaTodasNotasTituloPublico);
            var quantoQueroTotal = calculosCarteira.calcularQuantoQuero(carteiraIdealPorcento, totalCarteira);
            var valorTotalAtivo = aporteService.calcularValorTotalAtivo(titulo.getAportes());
            if (titulo.getValorRendimento() != null)
                valorTotalAtivo += titulo.getValorRendimento();
            var quantoFaltaTotal = calculosCarteira.calcularQuantoFalta(quantoQueroTotal, valorTotalAtivo);
            var precoMedio = calculosCarteira.calcularPrecoMedioAportes(titulo.getAportes());
            var precoParaCalculoQuantoFalta = precoMedio == 0 ? titulo.getPrecoInicial() : precoMedio;
            var quantidadeQueFaltaTotal = (int) Math.round(calculosCarteira.calcularQuantidadeQueFalta(quantoFaltaTotal, precoParaCalculoQuantoFalta));
            var comprarOuAguardar = calculosCarteira.informarComprarOuAguardar(quantidadeQueFaltaTotal);
            var lucroOuPerda = titulo.getValorRendimento() != null ? titulo.getValorRendimento() : 0;

            response.add(tituloPublicoMapper.toTituloPublicoResponse(titulo,
                    Formatter.doubleToReal(precoMedio), Formatter.doubleToReal(valorTotalAtivo),
                    comprarOuAguardar, Formatter.doubleToReal(lucroOuPerda)));
        });

        Paginado<TituloPublicoResponse> paginado = Paginado.<TituloPublicoResponse>builder()
                .pagina(pagina)
                .tamanho(tamanho)
                .total(totalTitulos)
                .itens(response)
                .build();
        return paginado;
    }

    public long total(String descricao){
        if (descricao != null) {
            return tituloPublicoRepository.find("1=1 and dataRegistroRemocao is null and descricao like '%" + descricao + "%'").count();
        }
        return tituloPublicoRepository.find("dataRegistroRemocao is null").count();
    }
}
