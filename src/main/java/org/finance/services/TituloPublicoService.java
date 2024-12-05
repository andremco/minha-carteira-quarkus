package org.finance.services;

import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.finance.configs.ApiConfigProperty;
import org.finance.exceptions.NegocioException;
import org.finance.mappers.TituloPublicoMapper;
import org.finance.models.data.Acao;
import org.finance.models.data.Aporte;
import org.finance.models.data.Setor;
import org.finance.models.data.TituloPublico;
import org.finance.models.request.tituloPublico.EditarTituloPublicoRequest;
import org.finance.models.request.tituloPublico.SalvarTituloPublicoRequest;
import org.finance.models.response.Paginado;
import org.finance.models.response.acao.DetalharAcaoResponse;
import org.finance.models.response.tituloPublico.DetalharTituloPublicoResponse;
import org.finance.models.response.tituloPublico.TituloPublicoResponse;
import org.finance.repositories.AcaoRepository;
import org.finance.repositories.SetorRepository;
import org.finance.repositories.TituloPublicoRepository;
import org.finance.utils.CalculosCarteira;
import org.finance.utils.Formatter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class TituloPublicoService {
    @Inject
    TituloPublicoRepository tituloPublicoRepository;
    @Inject
    AcaoRepository acaoRepository;
    @Inject
    SetorRepository setorRepository;
    @Inject
    TituloPublicoMapper tituloPublicoMapper;
    @Inject
    ApiConfigProperty apiConfigProperty;
    @Inject
    AporteService aporteService;
    @Inject
    CalculosCarteira calculosCarteira;

    public TituloPublicoResponse salvar(SalvarTituloPublicoRequest request){
        var existePorDescricao = tituloPublicoRepository.find("dataRegistroRemocao is null and descricao = :descricao",
                Parameters.with("descricao", request.getDescricao().trim())).count();

        if (existePorDescricao != 0)
            throw new NegocioException(apiConfigProperty.getRegistroJaExiste());

        var setor = setorRepository.findById(request.getSetorId().longValue());

        if (setor == null)
            throw new NegocioException(apiConfigProperty.getRegistroNaoEncontrado());

        var tituloPublico = tituloPublicoMapper.toTituloPublico(request, setor);
        tituloPublicoRepository.persist(tituloPublico);

        return tituloPublicoMapper.toTituloPublicoResponse(tituloPublico, setor);
    }

    public TituloPublicoResponse editar(EditarTituloPublicoRequest request){
        Setor setor = new Setor();

        if (request.getSetorId() != null)
            setor = setorRepository.findById(request.getSetorId().longValue());

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
            tituloPublico.setPrecoInicial(request.getPrecoInicial() );
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

        var totalCarteira = aporteService.calcularTotalCarteira();
        var somaTodasNotasCarteira = calculosCarteira.somarNotasCarteira(acaoRepository, tituloPublicoRepository);
        var precoMedio = calculosCarteira.calcularPrecoMedioAportes(tituloPublico.getAportes());
        var precoUltimoAporte = !tituloPublico.getAportes().isEmpty() ? tituloPublico.getAportes().getFirst().getPreco() : 0;
        var valorTotalAtivo = aporteService.calcularValorTotalAtivo(tituloPublico.getAportes());
        var carteiraIdealPorcento = calculosCarteira.calcularCarteiraIdealQuociente(tituloPublico.getNota(), somaTodasNotasCarteira);
        var carteiraTenhoPorcento = calculosCarteira.calcularCarteiraTenhoQuociente(valorTotalAtivo, totalCarteira);
        var quantoQueroTotal = calculosCarteira.calcularQuantoQuero(carteiraIdealPorcento, totalCarteira);
        var quantoFaltaTotal = calculosCarteira.calcularQuantoFalta(quantoQueroTotal, valorTotalAtivo);
        var precoParaCalculoQuantoFalta = precoMedio == 0 ? tituloPublico.getPrecoInicial() : precoMedio;
        var quantidadeQueFaltaTotal = (int) Math.round(calculosCarteira.calcularQuantidadeQueFalta(quantoFaltaTotal, precoParaCalculoQuantoFalta));
        var comprarOuAguardar = calculosCarteira.informarComprarOuAguardar(quantidadeQueFaltaTotal);
        var precoCalcularTotalAtivoAtual = precoUltimoAporte == 0 ? tituloPublico.getPrecoInicial() : precoUltimoAporte;
        var valorTotalAtivoAtual = aporteService.calcularValorTotalAtivoAtual(tituloPublico.getAportes(), precoCalcularTotalAtivoAtual);
        var lucroOuPerda = calculosCarteira.calcularLucroOuPerda(valorTotalAtivo, valorTotalAtivoAtual);

        return tituloPublicoMapper.toDetalharTituloPublicoResponse(tituloPublico, Formatter.doubleToReal(precoMedio),
                Formatter.doubleToPorcento(carteiraIdealPorcento), Formatter.doubleToPorcento(carteiraTenhoPorcento),
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

    public Paginado<TituloPublicoResponse> filtrarTitulos(Integer pagina, Integer tamanho){
        long totalTitulos = total();
        Paginado<TituloPublicoResponse> paginado = Paginado.<TituloPublicoResponse>builder()
                .pagina(pagina)
                .tamanho(tamanho)
                .total(totalTitulos)
                .itens(tituloPublicoMapper.toTitulosPublicoResponse(tituloPublicoRepository.findTitulosPaged(pagina, tamanho)))
                .build();
        return paginado;
    }

    public long total(){ return tituloPublicoRepository.find("dataRegistroRemocao is null").count(); }
}
