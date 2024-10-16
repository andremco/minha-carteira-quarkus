package org.finance.mappers;

import org.finance.models.data.Acao;
import org.finance.models.data.Setor;
import org.finance.models.request.acao.SalvarAcaoRequest;
import org.finance.models.response.acao.AcaoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

@Mapper(componentModel = "cdi", imports = {LocalDateTime.class})
public interface AcaoMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "setor", source = "setor")
    @Mapping(target = "dataRegistroCriacao", expression = "java(LocalDateTime.now())")
    @Mapping(target = "dataRegistroEdicao", ignore = true)
    @Mapping(target = "dataRegistroRemocao", ignore = true)
    Acao toAcao(SalvarAcaoRequest request, Setor setor);



    @Mapping(target = "id", source = "acao.id")
    @Mapping(target = "setorId", source = "setor.id")
    @Mapping(target = "setorDescricao", source = "setor.descricao")
    @Mapping(target = "dataRegistroCriacao", source = "acao.dataRegistroCriacao")
    AcaoResponse toAcaoResponse(Acao acao, Setor setor);
}
