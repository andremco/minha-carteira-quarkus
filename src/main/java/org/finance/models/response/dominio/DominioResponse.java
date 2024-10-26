package org.finance.models.response.dominio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class DominioResponse {
    private Integer id;
    private String descricao;
}
