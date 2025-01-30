package org.finance.models.data.mariadb.queries;

import jakarta.persistence.Column;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.SqlResultSetMapping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@SqlResultSetMapping(
        name = "SetoresFatiadoMapping",
        classes = @ConstructorResult(
                targetClass = AportesTotalPorTipoAtivo.class,
                columns = {
                        @ColumnResult(name = "SETOR", type = String.class),
                        @ColumnResult(name = "TOTAL_APORTADO", type = BigDecimal.class)
                }
        )
)
@Builder
@Getter
@Setter
@AllArgsConstructor
public class SetoresFatiados {
    @Column(name = "SETOR")
    private String setor;
    @Column(name = "TOTAL_APORTADO")
    private BigDecimal totalAportado;
}
