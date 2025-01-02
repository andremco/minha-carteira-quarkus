package org.finance.models.response.acao;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DetalharAcaoResponse extends AcaoResponse{
    private String valorTotalAtivo;
    private String carteiraIdealPorcento;
    private String carteiraTenhoPorcento;
    private String quantoQueroTotal;
    private String quantoFaltaTotal;
    private int quantidadeQueFaltaTotal;
}
