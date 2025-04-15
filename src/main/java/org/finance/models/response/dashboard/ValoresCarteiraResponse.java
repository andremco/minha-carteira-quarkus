package org.finance.models.response.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ValoresCarteiraResponse {
    private double totalCarteira;
    private double totalCarteiraAtualizado;
    private double lucroOuPerda;
    private Boolean balancoPositivo;
}
