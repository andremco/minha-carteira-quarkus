package org.finance.models.response.acao;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DetalharAcaoResponse extends AcaoResponse{
    private double precoDinamico;
    private double valorTotalAtivo;
    private double valorTotalAtivoAtual;
    private double carteiraIdealQuociente;
    private double carteiraTenhoQuociente;
    private double quantoQueroTotal;
    private double quantoFaltaTotal;
    private double quantidadeQueFaltaTotal;
    private String comprarOuAguardar;
}
