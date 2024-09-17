package org.finance.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
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

    public Setor salvar(SetorRequest setorRequest) {

        if (setorRequest == null)
            return null;

        setorMapper.toSetor(setorRequest);

        Setor setor = setorMapper.toSetor(setorRequest);
        setorRepository.persist(setor);

        return setor;
    }

    public Setor editar(SetorRequest setorRequest) {

        if (setorRequest == null || setorRequest.getId() == null)
            return null;

        Setor entidadeSetor = setorRepository.findById(setorRequest.getId().longValue());

        if (entidadeSetor == null)
            return null;

        Setor setor = setorMapper.toSetor(setorRequest);
        setorRepository.persist(setor);

        return setor;
    }

    public List<Setor> all(){
        return setorRepository.listAll();
    }
}
