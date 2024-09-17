package org.finance.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.finance.exceptions.NegocioException;
import org.finance.mappers.SetorMapper;
import org.finance.models.data.Setor;
import org.finance.models.request.SetorRequest;
import org.finance.repositories.SetorRepository;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class SetorService {
    @Inject
    SetorRepository setorRepository;

    @Inject
    SetorMapper setorMapper;

    public Setor salvar(SetorRequest setorRequest) throws NegocioException {

        if (setorRequest == null || setorRequest.getDescricao() == null)
            throw new NegocioException("Request não informado!");

        setorMapper.toSetor(setorRequest);

        Setor setor = setorMapper.toSetor(setorRequest);
        setorRepository.persist(setor);

        return setor;
    }

    public Setor editar(SetorRequest setorRequest) throws NegocioException{

        if (setorRequest == null || setorRequest.getId() == null)
            throw new NegocioException("Request não informado!");

        Setor entidadeSetor = setorRepository.findById(setorRequest.getId().longValue());

        if (entidadeSetor == null)
            throw new NegocioException("Setor não encontrado");

        Setor setor = setorMapper.toSetor(setorRequest);
        setorRepository.persist(setor);

        return setor;
    }

    public List<Setor> all(){
        return setorRepository.listAll();
    }
}
