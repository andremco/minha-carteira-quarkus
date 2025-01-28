package org.finance.services;

import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.finance.configs.ApiConfigProperty;
import org.finance.exceptions.NegocioException;
import org.finance.mappers.AporteMapper;
import org.finance.models.data.mariadb.entities.Acao;
import org.finance.models.data.mariadb.entities.Aporte;
import org.finance.models.data.mariadb.entities.TituloPublico;
import org.finance.models.enums.TipoAtivoEnum;
import org.finance.models.request.aporte.EditarAporteRequest;
import org.finance.models.request.aporte.SalvarAporteRequest;
import org.finance.models.response.Paginado;
import org.finance.models.response.aporte.AporteResponse;
import org.finance.repositories.mariadb.AcaoRepository;
import org.finance.repositories.mariadb.AporteRepository;
import org.finance.repositories.mariadb.TituloPublicoRepository;
import org.finance.utils.Formatter;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class AporteService {
    @Inject
    AporteRepository aporteRepository;
    @Inject
    AcaoRepository acaoRepository;
    @Inject
    TituloPublicoRepository tituloPublicoRepository;
    @Inject
    AporteMapper aporteMapper;
    @Inject
    ApiConfigProperty apiConfigProperty;
    @Inject
    TickerService tickerService;
    public AporteResponse salvar(SalvarAporteRequest request) {
        if (request.getAcaoId() == null && request.getTituloPublicoId() == null ||
                request.getAcaoId() != null && request.getTituloPublicoId() != null)
            throw new NegocioException(apiConfigProperty.getAporteParmasInsuficiente());

        Acao acao = null;
        TituloPublico tituloPublico = null;

        if (request.getAcaoId() != null)
            acao = acaoRepository.findById(request.getAcaoId().longValue());

        if (request.getTituloPublicoId() != null)
            tituloPublico = tituloPublicoRepository.findById(request.getTituloPublicoId().longValue());

        if (acao == null && tituloPublico == null)
            throw new NegocioException(apiConfigProperty.getRegistroNaoEncontrado());

        //Validar venda para ativo de ação!!
        if (acao != null && request.getMovimentacao().equalsIgnoreCase("V")){
            var aportes = aporteRepository.find("acao.id", request.getAcaoId()).list();
            validarVendasAportes(aportes, request.getQuantidade(), request.getPreco());
        }

        //Validar venda para ativo de título público!!
        if (tituloPublico != null && request.getMovimentacao().equalsIgnoreCase("V")){
            var aportes = aporteRepository.find("tituloPublico.id", request.getTituloPublicoId()).list();
            validarVendasAportes(aportes, request.getQuantidade(), request.getPreco());
        }

        var aporte = aporteMapper.toAporte(request, acao, tituloPublico);
        aporteRepository.persist(aporte);

        return aporteMapper.toAporteResponse(aporte, acao, tituloPublico);
    }

    public AporteResponse editar(EditarAporteRequest request) throws NegocioException {
        Acao acao = new Acao();
        TituloPublico tituloPublico = new TituloPublico();
        List<Aporte> aportes;
        var hoje = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);

        Aporte aporte = aporteRepository.findById(request.getId().longValue());
        if (aporte == null)
            throw new NegocioException(apiConfigProperty.getRegistroNaoEncontrado());

        var dataOperacao = aporte.getDataRegistroCriacao().withHour(0).withMinute(0).withSecond(0);
        if (!dataOperacao.isEqual(hoje))
            throw new NegocioException(apiConfigProperty.getAporteDiaOperacaoNaoPermitida());

        if (request.getAcaoId() == null && request.getTituloPublicoId() == null ||
                request.getAcaoId() != null && request.getTituloPublicoId() != null)
            throw new NegocioException(apiConfigProperty.getAporteParmasInsuficiente());

        if (request.getAcaoId() != null){
            acao = acaoRepository.findById(request.getAcaoId().longValue());
            //Validar venda para ativo de ação!!
            aportes = aporteRepository.find("acao.id", request.getAcaoId()).list();
            validarVendasAportes(aportes, request.getQuantidade(), request.getPreco());
        }

        if (request.getTituloPublicoId() != null){
            tituloPublico = tituloPublicoRepository.findById(request.getTituloPublicoId().longValue());
            //Validar venda para ativo de título público!!
            aportes = aporteRepository.find("tituloPublico.id", request.getTituloPublicoId()).list();
            validarVendasAportes(aportes, request.getQuantidade(), request.getPreco());
        }

        if (acao == null && tituloPublico == null)
            throw new NegocioException(apiConfigProperty.getRegistroNaoEncontrado());

        if (request.getAcaoId() != null)
            aporte.setAcao(acao);
        if (request.getTituloPublicoId() != null)
            aporte.setTituloPublico(tituloPublico);
        if (request.getPreco() != null)
            aporte.setPreco(request.getPreco());
        if (request.getQuantidade() != null)
            aporte.setQuantidade(request.getQuantidade());
        if (request.getMovimentacao() != null)
            aporte.setMovimentacao(request.getMovimentacao().charAt(0));

        aporte.setDataRegistroEdicao(LocalDateTime.now());
        aporteRepository.persist(aporte);

        return aporteMapper.toAporteResponse(aporte, acao, tituloPublico);
    }

    public void excluir(Integer id) throws NegocioException {
        Aporte aporte = aporteRepository.findById(id.longValue());
        if (aporte == null)
            throw new NegocioException(apiConfigProperty.getRegistroNaoEncontrado());

        aporte.setDataRegistroRemocao(LocalDateTime.now());
        aporteRepository.persist(aporte);
    }

    public Paginado<AporteResponse> filtrarAportes(Integer tipoAtivoId, Integer ativoId, String dataInicio, String dataFim, Integer pagina, Integer tamanho){
        TipoAtivoEnum tipoAtivoEnum = null;
        LocalDateTime inicio = null;
        LocalDateTime fim = null;

        if (tipoAtivoId != null)
            tipoAtivoEnum = TipoAtivoEnum.getById(tipoAtivoId);

        if (dataInicio != null)
            inicio = Formatter.stringToLocalDateTime(dataInicio);
        if (dataFim != null)
            fim = Formatter.stringToLocalDateTime(dataFim).withHour(23).withMinute(59);

        if (inicio != null && fim != null && inicio.isAfter(fim))
            throw new NegocioException(apiConfigProperty.getCampoDataInicioSuperiorDataFim());

        long totalAportes = total(tipoAtivoEnum, ativoId, inicio, fim);
        Paginado<AporteResponse> paginado = Paginado.<AporteResponse>builder()
                .pagina(pagina)
                .tamanho(tamanho)
                .total(totalAportes)
                .itens(aporteMapper.toAportesResponse(aporteRepository.findAcoesPaged(tipoAtivoEnum, ativoId, inicio, fim, pagina, tamanho)))
                .build();
        return paginado;
    }

    public long total(TipoAtivoEnum tipoAtivo, Integer ativoId, LocalDateTime dataInicio, LocalDateTime dataFim){
        return aporteRepository.total(tipoAtivo, ativoId, dataInicio, dataFim);
    }

    @CacheResult(cacheName = "buscar-total-carteira")
    public double calcularTotalCarteira(){
        var comprasRealizadas = aporteRepository.find("dataRegistroRemocao is null and movimentacao = 'C'").stream().mapToDouble(a -> a.getQuantidade() * a.getPreco()).sum();
        var vendasRealizadas = aporteRepository.find("dataRegistroRemocao is null and movimentacao = 'V'").stream().mapToDouble(a -> a.getQuantidade() * a.getPreco()).sum();
        return comprasRealizadas-vendasRealizadas;
    }

    @CacheResult(cacheName = "buscar-total-carteira-atualizado")
    public double calcularTotalCarteiraAtualizado(){
        Double totalCarteiraAtualizado = 0.0;
        var acoes = acaoRepository.find("dataRegistroRemocao is null").list();
        var titulosPublicos = tituloPublicoRepository.find("dataRegistroRemocao is null").list();

        if (!acoes.isEmpty())
            for (Acao acao : acoes){
                if (!acao.getAportes().isEmpty()){
                    var ticker = acao.getTicker();
                    var response = tickerService.obter(ticker);
                    var precoDinamico = response.getPrecoDinamico();
                    totalCarteiraAtualizado +=  calcularValorTotalAtivoAtual(acao.getAportes(), precoDinamico);
                }
            }

        if (!titulosPublicos.isEmpty())
            for (TituloPublico tituloPublico : titulosPublicos){
                if (!tituloPublico.getAportes().isEmpty()){
                    var precoUltimoAporte = tituloPublico.getAportes().getFirst().getPreco();
                    var precoCalcularTotalAtivoAtual = precoUltimoAporte == 0 ? tituloPublico.getPrecoInicial() : precoUltimoAporte;
                    totalCarteiraAtualizado += calcularValorTotalAtivoAtual(tituloPublico.getAportes(), precoCalcularTotalAtivoAtual);
                }
            }

        return totalCarteiraAtualizado;
    }

    public void validarVendasAportes(List<Aporte> aportes, int quantidadesAVender, double precoVenda){
        var totalVenda = quantidadesAVender * precoVenda;
        if ((aportes == null || aportes.isEmpty()) && totalVenda > 0)
            throw new NegocioException(apiConfigProperty.getAporteVendaNaoPermitida());

        var comprasRealizadas = comprasRealizadas(aportes);
        var vendasRealizadas = vendasRealizadas(aportes);

        if ((vendasRealizadas + totalVenda) > comprasRealizadas){
            throw new NegocioException(apiConfigProperty.getAporteVendaNaoPermitida());
        }
    }

    public static Integer calcularQuantidadeCompras(List<Aporte> aportes){
        if(aportes.isEmpty())
            return 0;
        var quantidadeComprasRealizadas = aportes.stream().filter(a -> a.getMovimentacao() == 'C' && a.getDataRegistroRemocao() == null).mapToInt(Aporte::getQuantidade).sum();
        var quantidadeVendasRealizadas = aportes.stream().filter(a -> a.getMovimentacao() == 'V' && a.getDataRegistroRemocao() == null).mapToInt(Aporte::getQuantidade).sum();

        return quantidadeComprasRealizadas - quantidadeVendasRealizadas;
    }

    public double calcularValorTotalAtivo(List<Aporte> aportes){
        if(aportes.isEmpty())
            return 0;
        return comprasRealizadas(aportes)-vendasRealizadas(aportes);
    }

    private double comprasRealizadas(List<Aporte> aportes){
        return aportes.stream().filter(a -> a.getMovimentacao() == 'C' && a.getDataRegistroRemocao() == null).mapToDouble(a -> a.getQuantidade() * a.getPreco()).sum();
    }

    private double vendasRealizadas(List<Aporte> aportes){
        return aportes.stream().filter(a -> a.getMovimentacao() == 'V' && a.getDataRegistroRemocao() == null).mapToDouble(a -> a.getQuantidade() * a.getPreco()).sum();
    }

    public double calcularValorTotalAtivoAtual(List<Aporte> aportes, double preco){
        return calcularQuantidadeCompras(aportes) * preco;
    }

    public static String sinalizarCompraOuVenda(char movimentacao){
        return movimentacao == 'C' ? "Compra" : "Venda";
    }
}
