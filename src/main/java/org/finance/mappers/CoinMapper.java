package org.finance.mappers;

import org.finance.models.dto.integration.response.currency.QuoteCurrencyResponse;
import org.finance.models.response.coin.CoinResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

@Mapper(componentModel = "cdi", imports = LocalDateTime.class)
public interface CoinMapper {
    @Mapping(target = "nome", source = "name")
    @Mapping(target = "codigo", source = "code")
    @Mapping(target = "precoDinamico", source = "ask")
    @Mapping(target = "dataCotacao", source = "createDate")
    CoinResponse toCoinResponse(QuoteCurrencyResponse responseApi);
}
