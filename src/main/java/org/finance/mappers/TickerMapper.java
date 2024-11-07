package org.finance.mappers;

import org.finance.models.dto.integration.response.GetQuote;
import org.finance.models.response.ticker.TickerResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "cdi")
public interface TickerMapper {
    @Mapping(target = "razaoSocial", source = "companyName")
    @Mapping(target = "precoDinamico", source = "dynamicPrice")
    TickerResponse toTickerResponse(GetQuote quote);

    List<TickerResponse> toTickersResponse(List<GetQuote> quotes);
}
