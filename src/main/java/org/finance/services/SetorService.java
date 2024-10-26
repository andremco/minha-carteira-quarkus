package org.finance.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.finance.configs.ApiConfigProperty;
import org.finance.exceptions.NegocioException;
import org.finance.mappers.SetorMapper;
import org.finance.models.data.Setor;
import org.finance.models.request.setor.EditarSetorRequest;
import org.finance.models.request.setor.SalvarSetorRequest;
import org.finance.models.response.Paginado;
import org.finance.models.response.setor.SetorResponse;
import org.finance.repositories.SetorRepository;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class SetorService {
    @Inject
    SetorRepository setorRepository;
    @Inject
    SetorMapper setorMapper;
    @Inject
    ApiConfigProperty apiConfigProperty;

    public SetorResponse salvar(SalvarSetorRequest request) throws NegocioException {

        if(setorRepository.count("descricao", request.getDescricao()) != 0)
            throw new NegocioException(apiConfigProperty.getRegistroJaExiste());

        Setor setor = setorMapper.toSetor(request);
        setorRepository.persist(setor);

        return setorMapper.toSetorResponse(setor);
    }

    public SetorResponse editar(EditarSetorRequest request) throws NegocioException {

        Setor setor = setorRepository.findById(request.getId().longValue());

        if (setor == null)
            throw new NegocioException(apiConfigProperty.getRegistroNaoEncontrado());

        long totalPorDescricao = setorRepository.count("descricao", request.getDescricao());

        if (totalPorDescricao >= 1)
            throw new NegocioException(apiConfigProperty.getRegistroJaExiste());

        setor.setDataRegistroEdicao(LocalDateTime.now());
        setor.setDescricao(request.getDescricao());
        setorRepository.persist(setor);

        return setorMapper.toSetorResponse(setor);
    }

    public void excluir(Integer id) throws NegocioException {
        Setor setor = setorRepository.findById(id.longValue());

        if (setor == null)
            throw new NegocioException(apiConfigProperty.getRegistroNaoEncontrado());
        if (!setor.getAcoes().isEmpty())
            throw new NegocioException(apiConfigProperty.getSetorNaoPodeSerExcluido());

        setorRepository.delete(setor);
    }

    public Paginado<SetorResponse> filtrarSetores(Integer pagina, Integer tamanho){
        long totalSetores = total();
        Paginado<SetorResponse> paginado = Paginado.<SetorResponse>builder()
                .pagina(pagina)
                .tamanho(tamanho)
                .total(totalSetores)
                .itens(setorMapper.toSetoresResponse(setorRepository.findSetoresPaged(pagina, tamanho)))
                .build();
        return paginado;
    }

    public long total(){
        return setorRepository.count();
    }
}
