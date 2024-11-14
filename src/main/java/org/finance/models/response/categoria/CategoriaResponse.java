package org.finance.models.response.categoria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CategoriaResponse {
    private Integer id;
    private String descricao;
}
