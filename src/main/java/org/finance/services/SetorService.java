package org.finance.services;

import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.finance.configs.ApiConfigProperty;
import org.finance.exceptions.NegocioException;
import org.finance.mappers.SetorMapper;
import org.finance.models.data.mariadb.entities.TipoAtivo;
import org.finance.models.data.mariadb.entities.Setor;
import org.finance.models.data.mariadb.queries.SetoresTotalNotas;
import org.finance.models.enums.TipoAtivoEnum;
import org.finance.models.request.setor.EditarSetorRequest;
import org.finance.models.request.setor.SalvarSetorRequest;
import org.finance.models.response.Paginado;
import org.finance.models.response.setor.SetorResponse;
import org.finance.repositories.mariadb.TipoAtivoRepository;
import org.finance.repositories.mariadb.SetorRepository;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class SetorService {
    @Inject
    SetorRepository setorRepository;
    @Inject
    TipoAtivoRepository tipoAtivoRepository;
    @Inject
    SetorMapper setorMapper;
    @Inject
    ApiConfigProperty apiConfigProperty;

    public SetorResponse salvar(SalvarSetorRequest request) throws NegocioException {
        var tipoAtivo = tipoAtivoRepository.findById(request.getTipoAtivoId().longValue());

        if (tipoAtivo == null)
            throw new NegocioException(apiConfigProperty.getRegistroNaoEncontrado());

        List<Setor> setores = setorRepository.findByDescricao(request.getDescricao());

        if(setores != null && !setores.isEmpty() && setores.stream().anyMatch(s -> s.getTipoAtivo().getId().equals(request.getTipoAtivoId())))
            throw new NegocioException(apiConfigProperty.getRegistroJaExiste());

        Setor setor = setorMapper.toSetor(request, tipoAtivo);
        setorRepository.persist(setor);

        return setorMapper.toSetorResponse(setor);
    }

    public SetorResponse editar(EditarSetorRequest request) throws NegocioException {
        if (request.getDescricao() == null && request.getTipoAtivoId() == null)
            throw new NegocioException(apiConfigProperty.getSetorParamsInsuficiente());

        Setor setor = setorRepository.findById(request.getId().longValue());
        if (setor == null)
            throw new NegocioException(apiConfigProperty.getRegistroNaoEncontrado());

        TipoAtivo tipoAtivo = null;
        if (request.getTipoAtivoId() != null)
            tipoAtivo = tipoAtivoRepository.findById(request.getTipoAtivoId().longValue());

        List<Setor> setores = setorRepository.findByDescricao(request.getDescricao());

        if(setores != null && !setores.isEmpty() && setores.stream().anyMatch(s -> s.getTipoAtivo().getId().equals(request.getTipoAtivoId())))
            throw new NegocioException(apiConfigProperty.getRegistroJaExiste());

        if (request.getDescricao() != null)
            setor.setDescricao(request.getDescricao());
        if (tipoAtivo != null)
            setor.setTipoAtivo(tipoAtivo);
        setor.setDataRegistroEdicao(LocalDateTime.now());
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

    public Setor buscarPorId(Integer id){
        if (id == null)
            return null;
        return setorRepository.findById(id.longValue());
    }

    public long total(){
        return setorRepository.count();
    }

    @CacheResult(cacheName = "buscar-setores-notas")
    public List<SetoresTotalNotas> obterSetoresTotalNotas(TipoAtivoEnum tipoAtivo){
        return setorRepository.obterSetoresTotalNotas(tipoAtivo);
    }
}
