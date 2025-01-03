package org.finance.models.response.tipoAtivo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class TipoAtivoResponse {
    private Integer id;
    private String descricao;
}
