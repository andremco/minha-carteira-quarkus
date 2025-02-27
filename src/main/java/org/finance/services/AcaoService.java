package org.finance.services;

import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.finance.configs.ApiConfigProperty;
import org.finance.exceptions.NegocioException;
import org.finance.mappers.AcaoMapper;
import org.finance.models.data.mariadb.entities.Acao;
import org.finance.models.data.mariadb.entities.Setor;
import org.finance.models.data.mariadb.entities.TituloPublico;
import org.finance.models.enums.TipoAtivoEnum;
import org.finance.models.request.acao.EditarAcaoRequest;
import org.finance.models.request.acao.SalvarAcaoRequest;
import org.finance.models.response.Paginado;
import org.finance.models.response.acao.AcaoResponse;
import org.finance.models.response.acao.DetalharAcaoResponse;
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
public class AcaoService {
    @Inject
    AcaoRepository acaoRepository;
    @Inject
    AcaoMapper acaoMapper;
    @Inject
    ApiConfigProperty apiConfigProperty;
    @Inject
    TickerService tickerService;
    @Inject
    AporteService aporteService;
    @Inject
    DashboardService dashboardService;
    @Inject
    SetorService setorService;
    @Inject
    CalculosCarteira calculosCarteira;

    public AcaoResponse salvar(SalvarAcaoRequest request) {
        var existePorRazaoSocial = acaoRepository.find("dataRegistroRemocao is null and razaoSocial = :razaoSocial",
                Parameters.with("razaoSocial", request.getRazaoSocial())).count();
        var existePorTicker = acaoRepository.find("dataRegistroRemocao is null and ticker = :ticker",
                Parameters.with("ticker", request.getTicker())).count();

        if(existePorRazaoSocial != 0 || existePorTicker != 0)
            throw new NegocioException(apiConfigProperty.getRegistroJaExiste());

        var setor = setorService.buscarPorId(request.getSetorId());

        if (setor == null)
            throw new NegocioException(apiConfigProperty.getRegistroNaoEncontrado());

        var acao = acaoMapper.toAcao(request, setor);
        acaoRepository.persist(acao);

        Integer quantidadeAportes = 0;

        return acaoMapper.toAcaoResponse(acao, setor, quantidadeAportes);
    }

    public AcaoResponse editar(EditarAcaoRequest request) throws NegocioException {
        Setor setor = new Setor();

        if (request.getSetorId() != null)
            setor = setorService.buscarPorId(request.getSetorId());

        if (setor == null)
            throw new NegocioException(apiConfigProperty.getRegistroNaoEncontrado());

        Acao acao = acaoRepository.findById(request.getId().longValue());
        if (acao == null)
            throw new NegocioException(apiConfigProperty.getRegistroNaoEncontrado());

        var findPorRazaoSocial = acaoRepository.find("dataRegistroRemocao is null and razaoSocial = :razaoSocial",
                Parameters.with("razaoSocial", request.getRazaoSocial())).firstResult();
        var findPorTicker = acaoRepository.find("dataRegistroRemocao is null and ticker = :ticker",
                Parameters.with("ticker", request.getTicker())).firstResult();

        if (findPorRazaoSocial != null && !Objects.equals(findPorRazaoSocial.getId(), request.getId()) ||
                findPorTicker != null && !Objects.equals(findPorTicker.getId(), request.getId()))
            throw new NegocioException(apiConfigProperty.getRegistroJaExiste());

        if (request.getRazaoSocial() != null)
            acao.setRazaoSocial(request.getRazaoSocial());
        if (request.getSetorId() != null)
            acao.setSetor(setor);
        if (request.getTicker() != null)
            acao.setTicker(request.getTicker());
        if (request.getNota() != null)
            acao.setNota(request.getNota());

        acao.setDataRegistroEdicao(LocalDateTime.now());
        acaoRepository.persist(acao);

        var quantidadeCompras = AporteService.calcularQuantidadeCompras(acao.getAportes());

        return acaoMapper.toAcaoResponse(acao, setor, quantidadeCompras);
    }

    public DetalharAcaoResponse detalharAcao(Integer id){
        Acao acao = acaoRepository.findById(id.longValue());
        if (acao == null)
            throw new NegocioException(apiConfigProperty.getRegistroNaoEncontrado());

        var ticker = tickerService.obter(acao.getTicker());
        if (ticker == null)
            throw new NegocioException(apiConfigProperty.getRegistroNaoEncontrado());

        var tipoAtivo = acao.getSetor().getTipoAtivo().getId();
        TipoAtivoEnum tipoAtivoEnum = TipoAtivoEnum.getById(tipoAtivo);

        var setores = setorService.obterSetoresTotalNotas(tipoAtivoEnum);
        var somaTodasNotasSetoresPorAtivo = calculosCarteira.somarNotasPorSetores(setores);

        var aportes = dashboardService.obterAportesTotal(null, null);
        var totalCarteira = switch (tipoAtivoEnum) {
            case TipoAtivoEnum.ACAO -> aportes.getTotalAcoes();
            case TipoAtivoEnum.FUNDO_IMOBILIARIO -> aportes.getTotalFIIs();
            case TipoAtivoEnum.BRAZILIAN_DEPOSITARY_RECEIPTS -> aportes.getTotalBDRs();
            default -> 0;
        };

        var quantidadeCompras = AporteService.calcularQuantidadeCompras(acao.getAportes());
        var valorTotalCompras = aporteService.comprasRealizadas(acao.getAportes());
        var valorTotalVendas = aporteService.vendasRealizadas(acao.getAportes());
        var valorTotalAtivo = aporteService.calcularValorTotalAtivo(acao.getAportes());
        var valorTotalAtivoAtual = aporteService.calcularValorTotalAtivoAtual(acao.getAportes(), ticker.getPrecoDinamico());
        var carteiraIdealPorcento = calculosCarteira.calcularCarteiraIdealQuociente(acao.getNota(), somaTodasNotasSetoresPorAtivo);
        var carteiraTenhoPorcento = calculosCarteira.calcularCarteiraTenhoQuociente(valorTotalAtivo, totalCarteira.doubleValue());
        var quantoQueroTotal = calculosCarteira.calcularQuantoQuero(carteiraIdealPorcento, totalCarteira.doubleValue());
        var quantoFaltaTotal = calculosCarteira.calcularQuantoFalta(quantoQueroTotal, valorTotalAtivo);
        var quantidadeQueFaltaTotal = (int) Math.round(calculosCarteira.calcularQuantidadeQueFalta(quantoFaltaTotal, ticker.getPrecoDinamico()));
        var comprarOuAguardar = calculosCarteira.informarComprarOuAguardar(carteiraIdealPorcento, carteiraTenhoPorcento);
        var lucroOuPerda = calculosCarteira.calcularLucroOuPerda(valorTotalAtivo, valorTotalAtivoAtual);

        return acaoMapper.toDetalharAcaoResponse(acao, Formatter.doubleToReal(ticker.getPrecoDinamico()), quantidadeCompras,
                Formatter.doubleToPorcento(carteiraIdealPorcento), Formatter.doubleToPorcento(carteiraTenhoPorcento),
                Formatter.doubleToReal(valorTotalAtivo), Formatter.doubleToReal(valorTotalAtivoAtual),
                Formatter.doubleToReal(valorTotalCompras), Formatter.doubleToReal(valorTotalVendas),
                Formatter.doubleToReal(quantoQueroTotal), Formatter.doubleToReal(quantoFaltaTotal), quantidadeQueFaltaTotal,
                comprarOuAguardar, Formatter.doubleToReal(lucroOuPerda));
    }

    public void excluir(Integer id) throws NegocioException {
        Acao acao = acaoRepository.findById(id.longValue());
        if (acao == null)
            throw new NegocioException(apiConfigProperty.getRegistroNaoEncontrado());

        var quantidade = AporteService.calcularQuantidadeCompras(acao.getAportes());

        if (quantidade > 0)
            throw new NegocioException(apiConfigProperty.getAcaoNaoPodeSerExcluido());

        acao.setDataRegistroRemocao(LocalDateTime.now());
        acaoRepository.persist(acao);
    }

    public Paginado<AcaoResponse> filtrarAcoes(Integer tipoAtivoId, String razaoSocial, Integer pagina, Integer tamanho){
        TipoAtivoEnum tipoAtivoEnum = null;
        if (tipoAtivoId != null)
            tipoAtivoEnum = TipoAtivoEnum.getById(tipoAtivoId);

        long totalAcoes = total(tipoAtivoEnum, razaoSocial);
        var acoes = acaoRepository.findAcoesPaged(tipoAtivoEnum, razaoSocial, pagina, tamanho);

        List<AcaoResponse> response = new ArrayList<>();
        var aportes = dashboardService.obterAportesTotal(null, null);
        var totalCarteiraAcoes = aportes.getTotalAcoes().doubleValue();
        var totalCarteiraFIIs = aportes.getTotalFIIs().doubleValue();
        var totalCarteiraBDRs = aportes.getTotalBDRs().doubleValue();

        var setoresAcoes = setorService.obterSetoresTotalNotas(TipoAtivoEnum.ACAO);
        var somaTodasNotasSetoresPorAcoes = calculosCarteira.somarNotasPorSetores(setoresAcoes);

        var setoresFIIs = setorService.obterSetoresTotalNotas(TipoAtivoEnum.FUNDO_IMOBILIARIO);
        var somaTodasNotasSetoresPorFIIs = calculosCarteira.somarNotasPorSetores(setoresFIIs);

        var setoresBDRs = setorService.obterSetoresTotalNotas(TipoAtivoEnum.BRAZILIAN_DEPOSITARY_RECEIPTS);
        var somaTodasNotasSetoresPorBDRs = calculosCarteira.somarNotasPorSetores(setoresBDRs);

        acoes.forEach(acao -> {
            var ticker = tickerService.obter(acao.getTicker());
            if (ticker == null)
                throw new NegocioException(apiConfigProperty.getRegistroNaoEncontrado());

            var tipoAtivo =  TipoAtivoEnum.getById(acao.getSetor().getTipoAtivo().getId());

            var totalCarteira = switch (tipoAtivo) {
                case TipoAtivoEnum.ACAO -> totalCarteiraAcoes;
                case TipoAtivoEnum.FUNDO_IMOBILIARIO -> totalCarteiraFIIs;
                case TipoAtivoEnum.BRAZILIAN_DEPOSITARY_RECEIPTS -> totalCarteiraBDRs;
                default -> 0;
            };

            var somaTodasNotasCarteira = switch (tipoAtivo) {
                case TipoAtivoEnum.ACAO -> somaTodasNotasSetoresPorAcoes;
                case TipoAtivoEnum.FUNDO_IMOBILIARIO -> somaTodasNotasSetoresPorFIIs;
                case TipoAtivoEnum.BRAZILIAN_DEPOSITARY_RECEIPTS -> somaTodasNotasSetoresPorBDRs;
                default -> 0;
            };

            var valorTotalAtivo = aporteService.calcularValorTotalAtivo(acao.getAportes());
            var valorTotalAtivoAtual = aporteService.calcularValorTotalAtivoAtual(acao.getAportes(), ticker.getPrecoDinamico());
            var lucroOuPerda = calculosCarteira.calcularLucroOuPerda(valorTotalAtivo, valorTotalAtivoAtual);
            var carteiraIdealPorcento = calculosCarteira.calcularCarteiraIdealQuociente(acao.getNota(), somaTodasNotasCarteira);
            var carteiraTenhoPorcento = calculosCarteira.calcularCarteiraTenhoQuociente(valorTotalAtivo, totalCarteira);
            var comprarOuAguardar = calculosCarteira.informarComprarOuAguardar(carteiraIdealPorcento, carteiraTenhoPorcento);

            response.add(acaoMapper.toAcaoResponse(acao, Formatter.doubleToReal(ticker.getPrecoDinamico()),
                    Formatter.doubleToReal(valorTotalAtivoAtual), comprarOuAguardar,
                    Formatter.doubleToReal(lucroOuPerda)));
        });

        Paginado<AcaoResponse> paginado = Paginado.<AcaoResponse>builder()
                .pagina(pagina)
                .tamanho(tamanho)
                .total(totalAcoes)
                .itens(response)
                .build();
        return paginado;
    }

    public long total(TipoAtivoEnum tipoAtivo, String razaoSocial){
        return acaoRepository.total(tipoAtivo, razaoSocial);
    }
}
