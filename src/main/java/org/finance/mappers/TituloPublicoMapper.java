package org.finance.mappers;

import org.finance.models.data.Setor;
import org.finance.models.data.TituloPublico;
import org.finance.models.request.tituloPublico.SalvarTituloPublicoRequest;
import org.finance.models.response.tituloPublico.TituloPublicoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "cdi", imports = LocalDateTime.class, uses = {SetorMapper.class})
public interface TituloPublicoMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "descricao", source = "request.descricao")
    @Mapping(target = "dataRegistroCriacao", expression = "java(LocalDateTime.now())")
    @Mapping(target = "dataRegistroEdicao", ignore = true)
    @Mapping(target = "dataRegistroRemocao", ignore = true)
    @Mapping(target = "aportes", ignore = true)
    TituloPublico toTituloPublico(SalvarTituloPublicoRequest request, Setor setor);

    @Mapping(target = "id", source = "tituloPublico.id")
    @Mapping(target = "descricao", source = "tituloPublico.descricao")
    @Mapping(target = "dataRegistro", source = "tituloPublico.dataRegistroCriacao")
    TituloPublicoResponse toTituloPublicoResponse(TituloPublico tituloPublico, Setor setor);

    @Mapping(target = "dataRegistro", source = "tituloPublico.dataRegistroCriacao")
    TituloPublicoResponse toTituloPublicoResponse(TituloPublico tituloPublico);

    List<TituloPublicoResponse> toTitulosPublicoResponse(List<TituloPublico> titulos);
}
