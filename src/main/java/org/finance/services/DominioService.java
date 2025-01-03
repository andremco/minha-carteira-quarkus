package org.finance.services;

import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.finance.mappers.DominioMapper;
import org.finance.models.data.Setor;
import org.finance.models.enums.TipoAtivoEnum;
import org.finance.models.response.dominio.DominioResponse;
import org.finance.repositories.TipoAtivoRepository;
import org.finance.repositories.SetorRepository;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class DominioService {
    @Inject
    SetorRepository setorRepository;
    @Inject
    TipoAtivoRepository tipoAtivoRepository;
    @Inject
    DominioMapper mapper;
    public List<DominioResponse> tipoAtivos(){
        var tipoAtivos = tipoAtivoRepository.findAll().list();
        return mapper.toTipoAtivoDominiosResponse(tipoAtivos);
    }
    public List<DominioResponse> setores(Integer tipoAtivo){
        TipoAtivoEnum tipoAtivoEnum = null;
        List<Setor> setores = new ArrayList<>();
        if (tipoAtivo != null){
            tipoAtivoEnum = TipoAtivoEnum.getById(tipoAtivo);
            setores = setorRepository.find("tipoAtivo.id = :tipoAtivoId",
                    Parameters.with("tipoAtivoId", tipoAtivoEnum.getId())).list();
        }
        else
            setores = setorRepository.findAll().list();

        return mapper.toSetorDominiosResponse(setores);
    }
}
