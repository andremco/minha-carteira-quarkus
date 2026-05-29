package org.finance.models.request.moeda;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SalvarMoedaRequest {
    @NotBlank(message = "{campo.nome.nao.informado}")
    @Size(max = 100, message = "{campo.nome.informado.chars.limite}")
    private String nome;
    @NotBlank(message = "{campo.codigo.nao.informado}")
    @Size(max = 10, message = "{campo.codigo.informado.chars.limite}")
    private String codigo;
    @NotNull(message = "{campo.nota.nao.informado}")
    @Min(value = 0, message = "{campo.nota.informado.valor.range}")
    @Max(value = 10, message = "{campo.nota.informado.valor.range}")
    private Integer nota;
}
