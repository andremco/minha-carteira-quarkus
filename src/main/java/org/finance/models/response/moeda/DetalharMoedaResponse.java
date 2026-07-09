package org.finance.models.response.moeda;

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
public class DetalharMoedaResponse extends MoedaResponse{
    private double valorTotalAtivo;
    private double valorTotalCompras;
    private double valorTotalVendas;
    private double carteiraIdealPorcento;
    private double carteiraTenhoPorcento;
    private double quantoQueroTotal;
    private double quantoFaltaTotal;
    private double quantidadeQueFaltaTotal;
}
