package org.finance.models.response.tituloPublico;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.finance.models.response.dominio.DominioResponse;
import org.finance.models.response.setor.SetorResponse;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class TituloPublicoResponse {
    private Integer id;
    private DominioResponse setor;
    private String descricao;
    private String precoMedio;
    private String valorTotalAtivo;
    private String precoInicial;
    private Integer nota;
    private Integer quantidade;
    private String lucroOuPerda;
    private String comprarOuAguardar;
    private String dataRegistro;
}
