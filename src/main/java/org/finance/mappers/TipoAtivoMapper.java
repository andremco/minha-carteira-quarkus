package org.finance.mappers;

import org.finance.models.data.mariadb.entities.TipoAtivo;
import org.finance.models.response.tipoAtivo.TipoAtivoResponse;
import org.finance.models.response.dominio.DominioResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "jakarta-cdi")
public interface TipoAtivoMapper {
    TipoAtivoResponse toTipoAtivoResponse(TipoAtivo tipoAtivo);
    DominioResponse toDominioResponse(TipoAtivo tipoAtivo);
}
