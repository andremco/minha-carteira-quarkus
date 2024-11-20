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
    private double tenhoTotalPorAportes;
    private double tenhoTotalPorPrecoAtual;
    private double carteiraIdeal;
    private double carteiraTenho;
}
