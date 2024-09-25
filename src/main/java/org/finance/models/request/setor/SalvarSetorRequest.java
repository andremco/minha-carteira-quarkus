package org.finance.models.request.setor;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SalvarSetorRequest {
    @NotBlank(message = "{campo.descricao.nao.informado}")
    private String descricao;
}
