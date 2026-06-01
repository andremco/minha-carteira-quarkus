package org.finance.mappers;

import org.finance.models.data.mariadb.entities.Moeda;
import org.finance.models.request.moeda.SalvarMoedaRequest;
import org.finance.models.response.coin.CoinResponse;
import org.finance.models.response.moeda.MoedaResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

@Mapper(componentModel = "cdi", imports = LocalDateTime.class)
public interface MoedaMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "codigo", expression = "java(request.getCodigo().toUpperCase())")
    @Mapping(target = "dataRegistroCriacao", expression = "java(LocalDateTime.now())")
    @Mapping(target = "dataRegistroEdicao", ignore = true)
    @Mapping(target = "dataRegistroRemocao", ignore = true)
    Moeda toMoeda(SalvarMoedaRequest request);

    @Mapping(target = "quantidade", ignore = true)
    @Mapping(target = "precoDinamico", ignore = true)
    @Mapping(target = "lucroOuPerda", ignore = true)
    @Mapping(target = "comprarOuAguardar", ignore = true)
    @Mapping(target = "valorTotalAtivoAtual", ignore = true)
    @Mapping(target = "dataRegistro", source = "dataRegistroCriacao")
    MoedaResponse toMoedaResponse(Moeda moeda);
}
