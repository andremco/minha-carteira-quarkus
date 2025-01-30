package org.finance.models.response.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class AportesTotalResponse {
    private BigDecimal porAcoes;
    private BigDecimal porFIIs;
    private BigDecimal porBDRs;
    private BigDecimal porTitulos;
}
