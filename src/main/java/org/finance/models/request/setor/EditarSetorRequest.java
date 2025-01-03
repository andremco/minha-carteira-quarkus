package org.finance.models.request.setor;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EditarSetorRequest {
    @NotNull(message = "{campo.id.nao.informado}")
    private Integer id;
    @Size(max = 60, message = "{ccampo.descricao.informado.chars.limite}")
    private String descricao;
    private Integer tipoAtivoId;
}
