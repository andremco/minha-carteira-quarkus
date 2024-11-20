package org.finance.mappers;

import org.finance.models.data.Categoria;
import org.finance.models.response.categoria.CategoriaResponse;
import org.finance.models.response.dominio.DominioResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "jakarta-cdi")
public interface CategoriaMapper {
    CategoriaResponse toCategoriaResponse(Categoria categoria);
    DominioResponse toDominioResponse(Categoria categoria);
}
