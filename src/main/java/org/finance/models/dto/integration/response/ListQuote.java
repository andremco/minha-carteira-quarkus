package org.finance.models.dto.integration.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ListQuote {
    @JsonProperty("close")
    private double dynamicPrice;
    @JsonProperty("stock")
    private String ticker;
    @JsonProperty("logo")
    private String logoUrl;
    private String sector;
}
