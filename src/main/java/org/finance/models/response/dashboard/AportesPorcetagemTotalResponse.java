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
public class AportesPorcetagemTotalResponse {
    private Double porAcoes;
    private Double porFIIs;
    private Double porBDRs;
    private Double porTitulos;
}
