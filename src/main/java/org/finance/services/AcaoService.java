package org.finance.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.finance.configs.ApiConfigProperty;
import org.finance.exceptions.NegocioException;
import org.finance.mappers.AcaoMapper;
import org.finance.models.data.*;
import org.finance.models.request.acao.EditarAcaoRequest;
import org.finance.models.request.acao.SalvarAcaoRequest;
import org.finance.models.response.Paginado;
import org.finance.models.response.acao.AcaoResponse;
import org.finance.models.response.acao.DetalharAcaoResponse;
import org.finance.repositories.AcaoRepository;
import org.finance.repositories.CategoriaRepository;
import org.finance.repositories.SetorRepository;
import org.finance.repositories.TituloPublicoRepository;
import org.finance.utils.Formatter;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@ApplicationScoped
public class AcaoService {
    @Inject
    AcaoRepository acaoRepository;
    @Inject
    TituloPublicoRepository tituloPublicoRepository;
    @Inject
    SetorRepository setorRepository;
    @Inject
    CategoriaRepository categoriaRepository;
    @Inject
    AcaoMapper acaoMapper;
    @Inject
    ApiConfigProperty apiConfigProperty;
    @Inject
    TickerService tickerService;
    @Inject
    AporteService aporteService;

    public AcaoResponse salvar(SalvarAcaoRequest request) {
        if(acaoRepository.count("razaoSocial", request.getRazaoSocial()) != 0 ||
                acaoRepository.count("ticker", request.getTicker()) != 0)
            throw new NegocioException(apiConfigProperty.getRegistroJaExiste());

        var setor = setorRepository.findById(request.getSetorId().longValue());
        var categoria = categoriaRepository.findById(request.getCategoriaId().longValue());

        if (setor == null || categoria == null)
            throw new NegocioException(apiConfigProperty.getRegistroNaoEncontrado());

        var acao = acaoMapper.toAcao(request, setor, categoria);
        acaoRepository.persist(acao);

        Integer quantidadeAportes = 0;

        return acaoMapper.toAcaoResponse(acao, setor, categoria, quantidadeAportes);
    }

    public AcaoResponse editar(EditarAcaoRequest request) throws NegocioException {
        Setor setor = new Setor();
        Categoria categoria = new Categoria();

        if (request.getSetorId() != null)
            setor = setorRepository.findById(request.getSetorId().longValue());

        if (request.getCategoriaId() != null)
            categoria = categoriaRepository.findById(request.getCategoriaId().longValue());

        if (categoria == null || setor == null)
            throw new NegocioException(apiConfigProperty.getRegistroNaoEncontrado());

        Acao acao = acaoRepository.findById(request.getId().longValue());
        if (acao == null)
            throw new NegocioException(apiConfigProperty.getRegistroNaoEncontrado());

        long totalPorRazaoSocial = acaoRepository.count("razaoSocial", request.getRazaoSocial());
        long totalPorTicker = acaoRepository.count("ticker", request.getTicker());

        if (totalPorRazaoSocial >= 1 || totalPorTicker >= 1)
            throw new NegocioException(apiConfigProperty.getRegistroJaExiste());

        if (request.getRazaoSocial() != null)
            acao.setRazaoSocial(request.getRazaoSocial());
        if (request.getSetorId() != null)
            acao.setSetor(setor);
        if (request.getCategoriaId() != null)
            acao.setCategoria(categoria);
        if (request.getTicker() != null)
            acao.setTicker(request.getTicker());
        if (request.getNota() != null)
            acao.setNota(request.getNota());

        acao.setDataRegistroEdicao(LocalDateTime.now());
        acaoRepository.persist(acao);

        var quantidadeCompras = aporteService.calcularQuantidadeCompras(acao.getAportes());

        return acaoMapper.toAcaoResponse(acao, setor, categoria, quantidadeCompras);
    }

    public DetalharAcaoResponse detalharAcao(Integer id){
        Acao acao = acaoRepository.findById(id.longValue());
        if (acao == null)
            throw new NegocioException(apiConfigProperty.getRegistroNaoEncontrado());

        var ticker = tickerService.obter(acao.getTicker());
        if (ticker == null)
            throw new NegocioException(apiConfigProperty.getRegistroNaoEncontrado());

        var totalCarteira = aporteService.calcularTotalCarteira();
        var quantidadeCompras = AporteService.calcularQuantidadeCompras(acao.getAportes());
        var valorTotalAtivo = aporteService.calcularValorTotalAtivo(acao.getAportes());
        var valorTotalAtivoAtual = aporteService.calcularValorTotalAtivoAtual(acao.getAportes(), ticker.getPrecoDinamico());
        var carteiraIdealPorcento = calcularCarteiraIdealQuociente(acao.getNota(), somaTodasNotasCarteira());
        var carteiraTenhoPorcento = calcularCarteiraTenhoQuociente(valorTotalAtivo, totalCarteira);
        var quantoQueroTotal = calcularQuantoQuero(carteiraIdealPorcento, totalCarteira);
        var quantoFaltaTotal = calcularQuantoFalta(quantoQueroTotal, valorTotalAtivo);
        var quantidadeQueFaltaTotal = calcularQuantidadeQueFalta(quantoFaltaTotal, ticker.getPrecoDinamico());
        var comprarOuAguardar = quantidadeQueFaltaTotal > 0 ? "Comprar" : "Aguardar";
        var lucroOuPerda = calcularLucroOuPerda(valorTotalAtivo, valorTotalAtivoAtual);

        return acaoMapper.toDetalharAcaoResponse(acao, Formatter.doubleToReal(ticker.getPrecoDinamico()), quantidadeCompras,
                carteiraIdealPorcento*100, carteiraTenhoPorcento*100, Formatter.doubleToReal(valorTotalAtivo),
                Formatter.doubleToReal(valorTotalAtivoAtual), Formatter.doubleToReal(quantoQueroTotal), quantoFaltaTotal, quantidadeQueFaltaTotal,
                comprarOuAguardar, Formatter.doubleToReal(lucroOuPerda));
    }

    public Integer somaTodasNotasCarteira(){
        var notasAcao = acaoRepository.findAll().stream().mapToInt(Acao::getNota).sum();
        var notasTituloPublico = tituloPublicoRepository.findAll().stream().mapToInt(TituloPublico::getNota).sum();

        return notasAcao + notasTituloPublico;
    }

    public void excluir(Integer id) throws NegocioException {
        Acao acao = acaoRepository.findById(id.longValue());
        if (acao == null)
            throw new NegocioException(apiConfigProperty.getRegistroNaoEncontrado());

        acao.setDataRegistroRemocao(LocalDateTime.now());
        acaoRepository.persist(acao);
    }

    public Paginado<AcaoResponse> filtrarAcoes(Integer pagina, Integer tamanho){
        long totalAcoes = total();
        var itens = acaoMapper.toAcoesResponse(acaoRepository.findAcoesPaged(pagina, tamanho));
        itens.sort(Comparator.comparing(AcaoResponse::getQuantidade).reversed());
        Paginado<AcaoResponse> paginado = Paginado.<AcaoResponse>builder()
                .pagina(pagina)
                .tamanho(tamanho)
                .total(totalAcoes)
                .itens(itens)
                .build();
        return paginado;
    }

    public long total(){ return acaoRepository.count(); }

    public double calcularCarteiraIdealQuociente(Integer nota, Integer somaTodasNotasCarteira){
        return ((double)nota/somaTodasNotasCarteira);
    }

    public double calcularCarteiraTenhoQuociente(double valorTotalAtivo, double totalCarteira){
        if(valorTotalAtivo == 0)
            return 0;
        return (valorTotalAtivo/totalCarteira);
    }

    public double calcularQuantoQuero(double carteiraIdeal, double totalCarteira){
        return carteiraIdeal*totalCarteira;
    }

    public double calcularQuantoFalta(double quantoQuero, double valorTotalAtivo){
        var quantoFalta = quantoQuero-valorTotalAtivo;
        return quantoFalta >= 0 ? quantoFalta : 0;
    }

    public double calcularQuantidadeQueFalta(double quantoFaltaTotal, double precoDinamico){
        double quantidadeQueFalta = quantoFaltaTotal/precoDinamico;
        return quantidadeQueFalta >= 0 ? quantidadeQueFalta : 0;
    }

    public double calcularLucroOuPerda(double valorTotalAtivo, double valorTotalAtivoAtual){
        return valorTotalAtivoAtual-valorTotalAtivo;
    }
}
