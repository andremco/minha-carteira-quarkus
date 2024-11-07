package org.finance.models.dto.integration.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class ListQuoteResponse {
    private List<ListQuote> stocks;
    private List<String> availableSectors;
    private List<String> availableStockTypes;
}
