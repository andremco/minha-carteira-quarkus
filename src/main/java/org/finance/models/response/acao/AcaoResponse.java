package org.finance.models.response.acao;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.finance.models.response.categoria.CategoriaResponse;
import org.finance.models.response.dominio.DominioResponse;
import org.finance.models.response.setor.SetorResponse;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class AcaoResponse {
    private Integer id;
    private DominioResponse setor;
    private DominioResponse categoria;
    private String razaoSocial;
    private String ticker;
    private Integer nota;
    private Integer quantidade;
    private String dataRegistro;
}
