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
public class AportesValorTotalResponse {
    private BigDecimal totalAcoes;
    private BigDecimal totalFIIs;
    private BigDecimal totalBDRs;
    private BigDecimal totalTitulos;
}
