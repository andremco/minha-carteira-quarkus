package org.finance.models.dto.integration.response.currency;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuoteCurrencyResponse {
    @JsonProperty("code")
    private String code;
    @JsonProperty("codein")
    private String codeIn;
    @JsonProperty("name")
    private String name;
    @JsonProperty("high")
    private Double high;
    @JsonProperty("low")
    private Double low;
    @JsonProperty("varBid")
    private Double varBid;
    @JsonProperty("pctChange")
    private Double pctChange;
    @JsonProperty("bid")
    private Double bid;
    @JsonProperty("ask")
    private Double ask;
    @JsonProperty("timestamp")
    private String timestamp;
    @JsonProperty("create_date")
    private String createDate;
}
