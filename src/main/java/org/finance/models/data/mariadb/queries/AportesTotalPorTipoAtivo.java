package org.finance.models.data.mariadb.queries;

import jakarta.persistence.Column;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.SqlResultSetMapping;
import lombok.*;

import java.math.BigDecimal;

@SqlResultSetMapping(
        name = "AportesTotalPorTipoAtivoMapping",
        classes = @ConstructorResult(
                targetClass = AportesTotalPorTipoAtivo.class,
                columns = {
                        @ColumnResult(name = "TOTAL_ACOES", type = BigDecimal.class),
                        @ColumnResult(name = "TOTAL_FIIS", type = BigDecimal.class),
                        @ColumnResult(name = "TOTAL_BDRS", type = BigDecimal.class),
                        @ColumnResult(name = "TOTAL_TITULOS", type = BigDecimal.class)
                }
        )
)
@Builder
@Getter
@Setter
@AllArgsConstructor
public class AportesTotalPorTipoAtivo {
    @Column(name = "TOTAL_ACOES")
    private BigDecimal totalAcoes;
    @Column(name = "TOTAL_FIIS")
    private BigDecimal totalFIIs;
    @Column(name = "TOTAL_BDRS")
    private BigDecimal totalBDRs;
    @Column(name = "TOTAL_TITULOS")
    private BigDecimal totalTitulos;
}
