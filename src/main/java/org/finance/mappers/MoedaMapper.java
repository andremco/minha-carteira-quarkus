package org.finance.mappers;

import org.finance.models.data.mariadb.entities.Moeda;
import org.finance.models.request.moeda.SalvarMoedaRequest;
import org.finance.models.response.moeda.DetalharMoedaResponse;
import org.finance.models.response.moeda.MoedaResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "jakarta-cdi", imports = LocalDateTime.class)
public interface MoedaMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "codigo", expression = "java(request.getCodigo().toUpperCase())")
    @Mapping(target = "dataRegistroCriacao", expression = "java(LocalDateTime.now())")
    @Mapping(target = "dataRegistroEdicao", ignore = true)
    @Mapping(target = "dataRegistroRemocao", ignore = true)
    Moeda toMoeda(SalvarMoedaRequest request);

    @Mapping(target = "quantidade", ignore = true)
    @Mapping(target = "lucroOuPerda", ignore = true)
    @Mapping(target = "precoDinamico", ignore = true)
    @Mapping(target = "comprarOuAguardar", ignore = true)
    @Mapping(target = "valorTotalAtivoAtual", ignore = true)
    @Mapping(target = "dataRegistro", source = "moeda.dataRegistroCriacao")
    MoedaResponse toMoedaResponse(Moeda moeda);

    @Mapping(target = "dataRegistro", source = "moeda.dataRegistroCriacao")
    @Mapping(target = "quantidade", source = "quantidadeQueTenho")
    MoedaResponse toMoedaResponse(Moeda moeda, double precoDinamico, Integer quantidadeQueTenho, double valorTotalAtivoAtual,
                                  String comprarOuAguardar, double lucroOuPerda);

    @Mapping(target = "nome", source = "moeda.nome")
    @Mapping(target = "codigo", source = "moeda.codigo")
    @Mapping(target = "dataRegistro", source = "moeda.dataRegistroCriacao")
    @Mapping(target = "quantidade", source = "quantidadeQueTenho")
    DetalharMoedaResponse toDetalharMoedaResponse(Moeda moeda, double precoDinamico,
                                                 double carteiraIdealPorcento, double carteiraTenhoPorcento,
                                                 double valorTotalAtivo, double valorTotalAtivoAtual,
                                                 double valorTotalCompras, double valorTotalVendas,
                                                 double quantoQueroTotal, double quantoFaltaTotal,
                                                 Integer quantidadeQueTenho, Integer quantidadeQueFaltaTotal,
                                                 String comprarOuAguardar, double lucroOuPerda);

    List<MoedaResponse> toMoedasResponse(List<Moeda> moedas);
}
