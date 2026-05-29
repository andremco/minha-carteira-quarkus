package org.finance.models.request.moeda;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EditarMoedaRequest {
    @NotNull(message = "{campo.id.nao.informado}")
    private Integer id;
    @Size(max = 100, message = "{campo.nome.informado.chars.limite}")
    private String nome;
    @Size(max = 10, message = "{campo.codigo.informado.chars.limite}")
    private String codigo;
    @Min(value = 0, message = "{campo.nota.informado.valor.range}")
    @Max(value = 10, message = "{campo.nota.informado.valor.range}")
    private Integer nota;
}
