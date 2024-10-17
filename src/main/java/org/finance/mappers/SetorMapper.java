package org.finance.mappers;

import org.finance.models.data.Setor;
import org.finance.models.request.setor.EditarSetorRequest;
import org.finance.models.request.setor.SalvarSetorRequest;
import org.finance.models.response.setor.SetorResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "cdi", imports = {LocalDateTime.class})
public interface  SetorMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "acoes", ignore = true)
    @Mapping(target = "dataRegistroEdicao", ignore = true)
    @Mapping(target = "dataRegistroCriacao", expression = "java(LocalDateTime.now())")
    Setor toSetor(SalvarSetorRequest request);
    @Mapping(target = "acoes", ignore = true)
    @Mapping(target = "dataRegistroCriacao", ignore = true)
    @Mapping(target = "dataRegistroEdicao", expression = "java(LocalDateTime.now())")
    Setor toSetor(EditarSetorRequest request);
    @Mapping(target = "dataRegistro", source = "dataRegistroCriacao")
    @Mapping(target = "numAtivos", expression = "java(setor.getAcoes().size())")
    SetorResponse toSetorResponse(Setor setor);
    List<SetorResponse> toSetoresResponse(List<Setor> setor);
}
