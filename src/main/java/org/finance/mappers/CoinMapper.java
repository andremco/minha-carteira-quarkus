package org.finance.mappers;

import org.finance.models.data.mongo.Coin;
import org.finance.models.dto.integration.response.currency.QuoteCurrencyResponse;
import org.finance.models.response.coin.CoinResponse;
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
}
