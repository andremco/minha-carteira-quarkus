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
public class ListQuote {
    @JsonProperty("close")
    private double dynamicPrice;
    @JsonProperty("stock")
    private String ticker;
    @JsonProperty("logo")
    private String logoUrl;
    private String sector;
}
