package org.finance.models.response.tituloPublico;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.finance.models.response.setor.SetorResponse;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class TituloPublicoResponse {
    private Integer id;
    private SetorResponse setor;
    private String descricao;
    private double precoInicial;
    private Integer nota;
    private String dataRegistro;
}
