package org.finance.mappers;

import org.finance.models.data.Acao;
import org.finance.models.data.Setor;
import org.finance.models.request.acao.SalvarAcaoRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

@Mapper(componentModel = "cdi", imports = {LocalDateTime.class})
public interface AcaoMapper {
    @Mapping(target = "dataRegistroCriacao", expression = "java(LocalDateTime.now())")
    @Mapping(target = "dataRegistroEdicao", ignore = true)
    @Mapping(target = "dataRegistroRemocao", ignore = true)
    @Mapping(target = "setor", source = "setor")
    Acao toAcao(SalvarAcaoRequest request, Setor setor);
}
