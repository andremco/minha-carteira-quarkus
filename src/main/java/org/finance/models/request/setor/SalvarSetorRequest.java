package org.finance.models.request.setor;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SalvarSetorRequest {
    @NotBlank(message = "{campo.descricao.nao.informado}")
    @Size(max = 60, message = "{campo.descricao.informado.chars.limite}")
    private String descricao;
    @NotNull(message = "{campo.tipo.ativo.id.nao.informado}")
    private Integer tipoAtivoId;
}
