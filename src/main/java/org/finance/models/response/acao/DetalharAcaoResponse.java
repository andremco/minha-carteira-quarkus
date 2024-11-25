package org.finance.models.response.acao;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DetalharAcaoResponse extends AcaoResponse{
    private String precoDinamico;
    private String valorTotalAtivo;
    private String valorTotalAtivoAtual;
    private String carteiraIdealPorcento;
    private String carteiraTenhoPorcento;
    private String quantoQueroTotal;
    private String quantoFaltaTotal;
    private int quantidadeQueFaltaTotal;
    private String comprarOuAguardar;
    private String lucroOuPerda;
}
