package org.finance.models.response.acao;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DetalharAcaoResponse extends AcaoResponse{
    private double valorTotalAtivo;
    private double valorTotalCompras;
    private double valorTotalVendas;
    private double carteiraIdealPorcento;
    private double carteiraTenhoPorcento;
    private double quantoQueroTotal;
    private double quantoFaltaTotal;
    private int quantidadeQueFaltaTotal;
}
