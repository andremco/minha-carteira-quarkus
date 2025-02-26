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
    private String totalCarteira;
    private String totalCarteiraAtualizado;
    private String lucroOuPerda;
    private Boolean balancoPositivo;
}
