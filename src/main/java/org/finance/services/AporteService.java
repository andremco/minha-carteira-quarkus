package org.finance.services;

import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.finance.configs.ApiConfigProperty;
import org.finance.exceptions.NegocioException;
import org.finance.mappers.AporteMapper;
import org.finance.models.data.*;
import org.finance.models.request.acao.EditarAcaoRequest;
import org.finance.models.request.aporte.EditarAporteRequest;
import org.finance.models.request.aporte.SalvarAporteRequest;
import org.finance.models.response.Paginado;
import org.finance.models.response.acao.AcaoResponse;
import org.finance.models.response.aporte.AporteResponse;
import org.finance.repositories.AcaoRepository;
import org.finance.repositories.AporteRepository;
import org.finance.repositories.TituloPublicoRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
            validarVendasAportes(aportes, request.getQuantidade());
        }

        //Validar venda para ativo de título público!!
        if (acao != null && request.getMovimentacao().equalsIgnoreCase("V")){
            var aportes = aporteRepository.find("tituloPublico.id", request.getTituloPublicoId()).list();
            validarVendasAportes(aportes, request.getQuantidade());
        }

        var aporte = aporteMapper.toAporte(request, acao, tituloPublico);
        aporteRepository.persist(aporte);

        return aporteMapper.toAporteResponse(aporte, acao, tituloPublico);
    }

    public AporteResponse editar(EditarAporteRequest request) throws NegocioException {
        Acao acao = new Acao();
        TituloPublico tituloPublico = new TituloPublico();
        List<Aporte> aportes = new ArrayList<>();

        if (request.getAcaoId() == null && request.getTituloPublicoId() == null ||
                request.getAcaoId() != null && request.getTituloPublicoId() != null)
            throw new NegocioException(apiConfigProperty.getAporteParmasInsuficiente());

        if (request.getAcaoId() != null){
            acao = acaoRepository.findById(request.getAcaoId().longValue());
            //Validar venda para ativo de ação!!
            aportes = aporteRepository.find("acao.id", request.getAcaoId()).list();
            validarVendasAportes(aportes, request.getQuantidade());
        }

        if (request.getTituloPublicoId() != null){
            tituloPublico = tituloPublicoRepository.findById(request.getTituloPublicoId().longValue());
            //Validar venda para ativo de título público!!
            aportes = aporteRepository.find("tituloPublico.id", request.getTituloPublicoId()).list();
            validarVendasAportes(aportes, request.getQuantidade());
        }

        if (acao == null && tituloPublico == null)
            throw new NegocioException(apiConfigProperty.getRegistroNaoEncontrado());

        Aporte aporte = aporteRepository.findById(request.getId().longValue());
        if (aporte == null)
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

    public Paginado<AporteResponse> filtrarAportes(Integer pagina, Integer tamanho){
        long totalAportes = total();
        Paginado<AporteResponse> paginado = Paginado.<AporteResponse>builder()
                .pagina(pagina)
                .tamanho(tamanho)
                .total(totalAportes)
                .itens(aporteMapper.toAportesResponse(aporteRepository.findAcoesPaged(pagina, tamanho)))
                .build();
        return paginado;
    }

    public long total(){ return aporteRepository.count(); }

    @CacheResult(cacheName = "buscar-total-carteira")
    public double calcularTotalCarteira(){
        return aporteRepository.findAll().stream().filter(a -> a.getMovimentacao() == 'C').mapToDouble(a -> a.getQuantidade() * a.getPreco()).sum();
    }

    @CacheResult(cacheName = "buscar-total-carteira-atualizado")
    public double calcularTotalCarteiraAtualizado(){
        return aporteRepository.findAll().stream().filter(a -> a.getMovimentacao() == 'C').mapToDouble(a ->
        {
            Double preco;
            if (a.getAcao() != null){
                var ticker = a.getAcao().getTicker();
                var response = tickerService.obter(ticker);
                preco = response.getPrecoDinamico();
            }
            else{
                preco = a.getPreco();
            }
            return a.getQuantidade() * preco;
        }).sum();
    }

    public void validarVendasAportes(List<Aporte> aportes, int quantidadesAVender){
        if ((aportes == null || aportes.isEmpty()) && quantidadesAVender > 0)
            throw new NegocioException(apiConfigProperty.getAporteVendaNaoPermitida());

        var comprasRealizadas = aportes.stream().filter(a -> a.getMovimentacao() == 'C').mapToInt(Aporte::getQuantidade).sum();
        var vendasRealizadas = aportes.stream().filter(a -> a.getMovimentacao() == 'V').mapToInt(Aporte::getQuantidade).sum();

        if ((vendasRealizadas + quantidadesAVender) > comprasRealizadas){
            throw new NegocioException(apiConfigProperty.getAporteVendaNaoPermitida());
        }
    }

    public static Integer calcularQuantidadeCompras(List<Aporte> aportes){
        if(aportes.isEmpty())
            return 0;
        var comprasRealizadas = aportes.stream().filter(a -> a.getMovimentacao() == 'C').mapToInt(Aporte::getQuantidade).sum();
        var vendasRealizadas = aportes.stream().filter(a -> a.getMovimentacao() == 'V').mapToInt(Aporte::getQuantidade).sum();

        return comprasRealizadas - vendasRealizadas;
    }

    public double calcularValorTotalAtivo(List<Aporte> aportes){
        if(aportes.isEmpty())
            return 0;
        var somaValoresPorAtivo = aportes.stream().filter(a -> a.getMovimentacao() == 'C').mapToDouble(a -> a.getQuantidade() * a.getPreco()).sum();
        return somaValoresPorAtivo;
    }

    public double calcularValorTotalAtivoAtual(List<Aporte> aportes, double precoDinamico){
        return calcularQuantidadeCompras(aportes) * precoDinamico;
    }
}
