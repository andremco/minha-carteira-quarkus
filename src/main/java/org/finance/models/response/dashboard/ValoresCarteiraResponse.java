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
    String totalCarteira;
    String totalCarteiraAtualizado;
    String lucroOuPerda;
}
