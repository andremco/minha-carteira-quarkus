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
    private double valorTotalCompras;
    private double valorTotalVendas;
    private double carteiraIdealPorcento;
    private double carteiraTenhoPorcento;
    private double quantoQueroTotal;
    private double quantoFaltaTotal;
    private int quantidadeQueFaltaTotal;
}
