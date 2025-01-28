package org.finance.mappers;

import org.finance.models.data.mariadb.entities.TipoAtivo;
import org.finance.models.data.mariadb.entities.Setor;
import org.finance.models.response.dominio.DominioResponse;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "cdi", imports = {LocalDateTime.class})
public interface DominioMapper {
    DominioResponse toDominioResponse(Setor setor);
    List<DominioResponse> toSetorDominiosResponse(List<Setor> setores);
    DominioResponse toDominioResponse(TipoAtivo tipoAtivo);
    List<DominioResponse> toTipoAtivoDominiosResponse(List<TipoAtivo> tipoAtivos);
}
