package org.finance.models.data.mariadb.queries;

import jakarta.persistence.Column;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.SqlResultSetMapping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@SqlResultSetMapping(
        name = "SetoresTotalNotasMapping",
        classes = @ConstructorResult(
                targetClass = SetoresTotalNotas.class,
                columns = {
                        @ColumnResult(name = "SETOR", type = String.class),
                        @ColumnResult(name = "TOTAL_NOTAS_ATIVOS", type = Integer.class)
                }
        )
)
@Builder
@Getter
@Setter
@AllArgsConstructor
public class SetoresTotalNotas {
    @Column(name = "SETOR")
    private String setor;
    @Column(name = "TOTAL_NOTAS_ATIVOS")
    private Integer totalNotasAtivos;
}
