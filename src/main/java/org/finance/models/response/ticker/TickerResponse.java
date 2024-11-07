package org.finance.models.response.ticker;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class TickerResponse {
    private String razaoSocial;
    private String ticker;
    private double precoDinamico;
    private String logoUrl;
}
