package org.finance.models.request.tituloPublico;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EditarTituloPublicoRequest {
    @NotNull(message = "{campo.id.nao.informado}")
    private Integer id;
    private String descricao;
    private Integer setorId;
    private Double precoInicial;
    private Integer nota;
}
