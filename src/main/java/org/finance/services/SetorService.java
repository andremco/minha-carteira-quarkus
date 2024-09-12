package org.finance.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.finance.models.data.Setor;
import org.finance.models.request.SetorRequest;
import org.finance.repositories.SetorRepository;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class SetorService {
    @Inject
    private SetorRepository setorRepository;

    public Setor salvar(SetorRequest setorRequest) {

        if (setorRequest == null)
            return null;

        Setor setor = new Setor();
        setor.setDescricao(setorRequest.getDescricao());
        setor.setDataRegistro(LocalDateTime.now());
        setorRepository.persist(setor);

        return setor;
    }

    public List<Setor> listAll(){
        return setorRepository.listAll();
    }
}
