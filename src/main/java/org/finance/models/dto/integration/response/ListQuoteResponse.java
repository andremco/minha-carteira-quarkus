package org.finance.models.dto.integration.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ListQuoteResponse {
    private List<ListQuote> stocks;
    private List<String> availableSectors;
    private List<String> availableStockTypes;
}
