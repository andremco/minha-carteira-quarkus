package org.finance.mappers;

import org.finance.models.data.Categoria;
import org.finance.models.data.Setor;
import org.finance.models.response.dominio.DominioResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "cdi", imports = {LocalDateTime.class})
public interface DominioMapper {
    DominioResponse toDominioResponse(Setor setor);
    List<DominioResponse> toSetorDominiosResponse(List<Setor> setores);
    DominioResponse toDominioResponse(Categoria categoria);
    List<DominioResponse> toCategoriaDominiosResponse(List<Categoria> categorias);
}
