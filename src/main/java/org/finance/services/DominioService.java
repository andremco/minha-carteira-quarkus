package org.finance.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.finance.mappers.DominioMapper;
import org.finance.models.response.dominio.DominioResponse;
import org.finance.repositories.CategoriaRepository;
import org.finance.repositories.SetorRepository;

import java.util.List;

@ApplicationScoped
public class DominioService {
    @Inject
    SetorRepository setorRepository;
    @Inject
    CategoriaRepository categoriaRepository;
    @Inject
    DominioMapper mapper;
    public List<DominioResponse> categorias(){
        var categorias = categoriaRepository.findAll().list();
        return mapper.toCategoriaDominiosResponse(categorias);
    }
    public List<DominioResponse> setores(){
        var setores = setorRepository.findAll().list();
        return mapper.toSetorDominiosResponse(setores);
    }
}
