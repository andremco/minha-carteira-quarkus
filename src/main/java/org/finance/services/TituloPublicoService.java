package org.finance.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.finance.configs.ApiConfigProperty;
import org.finance.exceptions.NegocioException;
import org.finance.mappers.TituloPublicoMapper;
import org.finance.models.data.Setor;
import org.finance.models.data.TituloPublico;
import org.finance.models.request.tituloPublico.EditarTituloPublicoRequest;
import org.finance.models.request.tituloPublico.SalvarTituloPublicoRequest;
import org.finance.models.response.Paginado;
import org.finance.models.response.tituloPublico.TituloPublicoResponse;
import org.finance.repositories.SetorRepository;
import org.finance.repositories.TituloPublicoRepository;

import java.time.LocalDateTime;

@ApplicationScoped
public class TituloPublicoService {
    @Inject
    TituloPublicoRepository tituloPublicoRepository;
    @Inject
    SetorRepository setorRepository;
    @Inject
    TituloPublicoMapper tituloPublicoMapper;
    @Inject
    ApiConfigProperty apiConfigProperty;

    public TituloPublicoResponse salvar(SalvarTituloPublicoRequest request){
        if (tituloPublicoRepository.count("descricao", request.getDescricao()) != 0)
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

        long totalPorDescricao = tituloPublicoRepository.count("descricao", request.getDescricao());

        if (totalPorDescricao >= 1)
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

    public void excluir(Integer id) throws NegocioException {
        TituloPublico tituloPublico = tituloPublicoRepository.findById(id.longValue());
        if (tituloPublico == null)
            throw new NegocioException(apiConfigProperty.getRegistroNaoEncontrado());

        tituloPublico.setDataRegistroRemocao(LocalDateTime.now());
        tituloPublicoRepository.persist(tituloPublico);
    }

    public Paginado<TituloPublicoResponse> filtrarAcoes(Integer pagina, Integer tamanho){
        long totalAcoes = total();
        Paginado<TituloPublicoResponse> paginado = Paginado.<TituloPublicoResponse>builder()
                .pagina(pagina)
                .tamanho(tamanho)
                .total(totalAcoes)
                .itens(tituloPublicoMapper.toTitulosPublicoResponse(tituloPublicoRepository.findAcoesPaged(pagina, tamanho)))
                .build();
        return paginado;
    }

    public long total(){ return tituloPublicoRepository.count(); }
}
