package org.finance.models.request.setor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EditarSetorRequest {
    @NotNull(message = "{campo.id.nao.informado}")
    private Integer id;
    @NotBlank(message = "{campo.descricao.nao.informado}")
    private String descricao;
}
