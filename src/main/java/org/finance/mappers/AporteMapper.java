package org.finance.mappers;

import org.finance.models.data.*;
import org.finance.models.request.aporte.SalvarAporteRequest;
import org.finance.models.response.acao.AcaoResponse;
import org.finance.models.response.aporte.AporteResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "jakarta-cdi", imports = {LocalDateTime.class}, uses = {AcaoMapper.class, TituloPublicoMapper.class})
public interface AporteMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataRegistroCriacao", expression = "java(LocalDateTime.now())")
    @Mapping(target = "dataRegistroEdicao", ignore = true)
    @Mapping(target = "dataRegistroRemocao", ignore = true)
    Aporte toAporte(SalvarAporteRequest request, Acao acao, TituloPublico tituloPublico);

    @Mapping(target = "id", source = "acao.id")
    @Mapping(target = "dataRegistro", source = "acao.dataRegistroCriacao")
    AporteResponse toAporteResponse(Aporte aporte, Acao acao, TituloPublico tituloPublico);

    @Mapping(target = "dataRegistro", source = "aporte.dataRegistroCriacao")
    AporteResponse toAporteResponse(Aporte aporte);

    List<AporteResponse> toAportesResponse(List<Aporte> aportes);
}
