package org.finance.mappers;

import org.finance.models.data.mariadb.TipoAtivo;
import org.finance.models.data.mariadb.Setor;
import org.finance.models.request.setor.EditarSetorRequest;
import org.finance.models.request.setor.SalvarSetorRequest;
import org.finance.models.response.dominio.DominioResponse;
import org.finance.models.response.setor.SetorResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "jakarta-cdi", imports = {LocalDateTime.class, TipoAtivoMapper.class})
public interface  SetorMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "acoes", ignore = true)
    @Mapping(target = "titulosPublico", ignore = true)
    @Mapping(target = "dataRegistroEdicao", ignore = true)
    @Mapping(target = "descricao", source = "request.descricao")
    @Mapping(target = "dataRegistroCriacao", expression = "java(LocalDateTime.now())")
    Setor toSetor(SalvarSetorRequest request, TipoAtivo tipoAtivo);

    @Mapping(target = "id", source = "request.id")
    @Mapping(target = "acoes", ignore = true)
    @Mapping(target = "titulosPublico", ignore = true)
    @Mapping(target = "dataRegistroCriacao", ignore = true)
    @Mapping(target = "descricao", source = "request.descricao")
    @Mapping(target = "dataRegistroEdicao", expression = "java(LocalDateTime.now())")
    Setor toSetor(EditarSetorRequest request, TipoAtivo tipoAtivo);

    @Mapping(target = "dataRegistro", source = "setor.dataRegistroCriacao")
    @Mapping(target = "numAtivos", expression = "java(setor.getTotalAtivos())")
    SetorResponse toSetorResponse(Setor setor);

    DominioResponse toDominioResponse(Setor setor);

    List<SetorResponse> toSetoresResponse(List<Setor> setor);
}
