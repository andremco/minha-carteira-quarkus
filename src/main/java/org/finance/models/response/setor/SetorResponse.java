package org.finance.models.response.setor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.finance.models.response.dominio.DominioResponse;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class SetorResponse {
    private Integer id;
    private String descricao;
    private String dataRegistro;
    private Integer numAtivos;
    private DominioResponse tipoAtivo;
}
