package org.finance.models.dto.integration.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetQuote {
    @JsonProperty("longName")
    private String companyName;
    @JsonProperty("regularMarketPrice")
    private double dynamicPrice;
    @JsonProperty("symbol")
    private String ticker;
    @JsonProperty("logourl")
    private String logoUrl;
}
