package org.finance.mappers;

import org.finance.models.data.Setor;
import org.finance.models.request.SetorRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.time.LocalDateTime;

@Mapper(componentModel = "cdi", imports = {LocalDateTime.class})
public interface  SetorMapper {
    @Mapping(target = "dataRegistro", expression = "java(LocalDateTime.now())")
    Setor toSetor(SetorRequest request);
}
