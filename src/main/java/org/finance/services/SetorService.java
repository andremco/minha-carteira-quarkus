package org.finance.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.finance.exceptions.NegocioException;
import org.finance.mappers.SetorMapper;
import org.finance.models.data.Setor;
import org.finance.models.request.setor.EditarSetorRequest;
import org.finance.models.request.setor.SalvarSetorRequest;
import org.finance.repositories.SetorRepository;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class SetorService {
    @Inject
    SetorRepository setorRepository;

    @Inject
    SetorMapper setorMapper;

    public static final String SETOR_NAO_ENCONTRADO = "Setor não encontrado!";
    public static final String SETOR_JA_EXISTE = "Setor informado já existe!";
    public static final String SETOR_NAO_PODE_SER_EXCLUIDO = "Este setor possui vínculo não pode ser alterado!";

    public Setor salvar(SalvarSetorRequest request){

        if(setorRepository.count("descricao", request.getDescricao()) != 0)
            throw new NegocioException(SETOR_JA_EXISTE);

        Setor setor = setorMapper.toSetor(request);
        setorRepository.persist(setor);

        return setor;
    }

    public Setor editar(EditarSetorRequest request) throws NegocioException{

        Setor setor = setorRepository.findById(request.getId().longValue());

        if (setor == null)
            throw new NegocioException(SETOR_NAO_ENCONTRADO);

        long totalPorDescricao = setorRepository.count("descricao", request.getDescricao());

        if (totalPorDescricao >= 1)
            throw new NegocioException(SETOR_JA_EXISTE);

        setor.setDataRegistroEdicao(LocalDateTime.now());
        setor.setDescricao(request.getDescricao());
        setorRepository.persist(setor);

        return setor;
    }

    public void excluir(Integer id){
        Setor setor = setorRepository.findById(id.longValue());

        if (setor == null)
            throw new NegocioException(SETOR_NAO_ENCONTRADO);
        if (!setor.getAcoes().isEmpty())
            throw new NegocioException(SETOR_NAO_PODE_SER_EXCLUIDO);

        setorRepository.delete(setor);
    }

    public List<Setor> all(){
        return setorRepository.listAll();
    }
}
