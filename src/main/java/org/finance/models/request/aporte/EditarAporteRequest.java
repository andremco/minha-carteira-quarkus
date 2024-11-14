package org.finance.models.request.aporte;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EditarAporteRequest {
    @NotNull(message = "{campo.id.nao.informado}")
    private Integer id;
    private Integer acaoId;
    private Integer tituloPublicoId;
    private Double preco;
    private Integer quantidade;
    private String movimentacao;
}
