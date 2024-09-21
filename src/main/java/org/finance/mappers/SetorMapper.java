package org.finance.mappers;

import org.finance.models.data.Setor;
import org.finance.models.request.setor.EditarSetorRequest;
import org.finance.models.request.setor.SalvarSetorRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.time.LocalDateTime;

@Mapper(componentModel = "cdi", imports = {LocalDateTime.class})
public interface  SetorMapper {
    @Mapping(target = "dataRegistroCriacao", expression = "java(LocalDateTime.now())")
    @Mapping(target = "dataRegistroEdicao", ignore = true)
    Setor toSetor(SalvarSetorRequest request);
    @Mapping(target = "dataRegistroCriacao", ignore = true)
    @Mapping(target = "dataRegistroEdicao", expression = "java(LocalDateTime.now())")
    Setor toSetor(EditarSetorRequest request);
}
