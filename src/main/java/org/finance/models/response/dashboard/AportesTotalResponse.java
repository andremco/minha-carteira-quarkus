package org.finance.models.response.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class AportesTotalResponse {
    private double totalAcoes;
    private double totalFIIs;
    private double totalBDRs;
    private double totalTituloPublico;
}
