package org.finance.models.response.tituloPublico;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DetalharTituloPublicoResponse extends TituloPublicoResponse {
    private String carteiraIdealPorcento;
    private String carteiraTenhoPorcento;
    private String quantoQueroTotal;
    private String quantoFaltaTotal;
    private int quantidadeQueFaltaTotal;
}
