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
    @NotNull(message = "Campo id não informado!")
    private Integer id;
    @NotBlank(message = "Campo descrição não informado!")
    private String descricao;
}
