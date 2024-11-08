package org.finance.mappers;

import org.finance.models.dto.integration.response.GetQuote;
import org.finance.models.dto.integration.response.ListQuote;
import org.finance.models.response.ticker.TickerResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "cdi", imports = LocalDateTime.class)
public interface TickerMapper {
    @Mapping(target = "razaoSocial", source = "companyName")
    @Mapping(target = "precoDinamico", source = "dynamicPrice")
    @Mapping(target = "dataCotacao", expression = "java(LocalDateTime.now().toString())")
    TickerResponse toTickerResponse(GetQuote quote);

    List<TickerResponse> toTickersResponse(List<GetQuote> quotes);

    @Mapping(target = "razaoSocial", ignore = true)
    @Mapping(target = "precoDinamico", source = "dynamicPrice")
    @Mapping(target = "dataCotacao", expression = "java(LocalDateTime.now().toString())")
    TickerResponse toTickerResponse(ListQuote quote);

    List<TickerResponse> toTickersResponse(ListQuote[] quotes);
}
