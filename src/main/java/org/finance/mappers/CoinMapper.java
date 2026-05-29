package org.finance.mappers;

import org.finance.models.data.mariadb.entities.Moeda;
import org.finance.models.data.mongo.Coin;
import org.finance.models.dto.integration.response.currency.QuoteCurrencyResponse;
import org.finance.models.request.moeda.SalvarMoedaRequest;
import org.finance.models.response.coin.CoinResponse;
import org.finance.models.response.moeda.MoedaResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "cdi", imports = LocalDateTime.class)
public interface CoinMapper {
    @Mapping(target = "nome", source = "name")
    @Mapping(target = "codigo", source = "code")
    @Mapping(target = "precoDinamico", source = "bid")
    @Mapping(target = "dataCotacao", expression = "java(responseApi.getCreateDate().toString())")
    CoinResponse toCoinResponse(QuoteCurrencyResponse responseApi);

    List<CoinResponse> toCoinsResponse(List<QuoteCurrencyResponse> quotes);

    @Mapping(target = "priceBid", source = "bid")
    @Mapping(target = "quotationDate", source = "createDate")
    Coin toCoinMongoResponse(QuoteCurrencyResponse quote);

    @Mapping(target = "nome", source = "name")
    @Mapping(target = "codigo", source = "code")
    @Mapping(target = "precoDinamico", source = "priceBid")
    @Mapping(target = "dataCotacao", expression = "java(coin.getQuotationDate().toString())")
    CoinResponse toCoinResponse(Coin coin);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "codigo", expression = "java(request.getCodigo().toUpperCase())")
    @Mapping(target = "dataRegistroCriacao", expression = "java(LocalDateTime.now())")
    @Mapping(target = "dataRegistroEdicao", ignore = true)
    @Mapping(target = "dataRegistroRemocao", ignore = true)
    Moeda toMoeda(SalvarMoedaRequest request);

    @Mapping(target = "dataRegistro", source = "moeda.dataRegistroCriacao")
    @Mapping(target = "precoDinamico", ignore = true)
    @Mapping(target = "dataCotacao", ignore = true)
    MoedaResponse toMoedaResponse(Moeda moeda);

    @Mapping(target = "id", source = "moeda.id")
    @Mapping(target = "nome", source = "moeda.nome")
    @Mapping(target = "codigo", source = "moeda.codigo")
    @Mapping(target = "dataRegistro", source = "moeda.dataRegistroCriacao")
    @Mapping(target = "precoDinamico", source = "cotacao.precoDinamico")
    @Mapping(target = "dataCotacao", source = "cotacao.dataCotacao")
    MoedaResponse toMoedaResponse(Moeda moeda, CoinResponse cotacao);
}
