package org.finance.models.response.acao;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.finance.models.response.setor.SetorResponse;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class AcaoResponse {
    private Integer id;
    private SetorResponse setor;
    private String razaoSocial;
    private String ticker;
    private Integer nota;
    private Integer quantidade;
    private String precoDinamico;
    private String lucroOuPerda;
    private String comprarOuAguardar;
    private String valorTotalAtivoAtual;
    private String dataRegistro;
    private String urlIconAtivo;
}
