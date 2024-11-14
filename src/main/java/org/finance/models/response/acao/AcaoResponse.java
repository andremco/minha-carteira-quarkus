package org.finance.models.response.acao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.finance.models.response.categoria.CategoriaResponse;
import org.finance.models.response.setor.SetorResponse;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class AcaoResponse {
    private Integer id;
    private SetorResponse setor;
    private CategoriaResponse categoria;
    private String razaoSocial;
    private String ticker;
    private Integer nota;
    private String dataRegistro;
}
