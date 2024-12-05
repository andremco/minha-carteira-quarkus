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
    @Size(max = 100, message = "{campo.descricao.informado.chars.limite}")
    private String descricao;
    private Integer setorId;
    @Positive(message = "{campo.preco.inicial.valor.invalido}")
    private Double precoInicial;
    @Min(value = 0, message = "{campo.nota.informado.valor.range}")
    @Max(value = 10, message = "{campo.nota.informado.valor.range}")
    private Integer nota;
}
