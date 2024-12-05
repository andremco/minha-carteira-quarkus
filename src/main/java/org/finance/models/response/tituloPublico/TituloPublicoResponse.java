package org.finance.models.response.tituloPublico;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.finance.models.response.setor.SetorResponse;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class TituloPublicoResponse {
    private Integer id;
    private SetorResponse setor;
    private String descricao;
    private String precoInicial;
    private Integer nota;
    private Integer quantidade;
    private String dataRegistro;
}
