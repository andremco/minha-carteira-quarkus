package org.finance.models.request.tituloPublico;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SalvarTituloPublicoRequest {
    @NotNull(message = "{campo.setor.id.nao.informado}")
    private Integer setorId;
    @NotBlank(message = "{campo.descricao.nao.informado}")
    @Size(max = 100, message = "{campo.descricao.informado.chars.limite}")
    private String descricao;
    @NotNull(message = "{campo.preco.inicial.nao.informado}")
    @Positive(message = "{campo.preco.inicial.valor.invalido}")
    private Double precoInicial;
    @NotNull(message = "{campo.nota.nao.informado}")
    @Min(value = 0, message = "{campo.nota.informado.valor.range}")
    @Max(value = 10, message = "{campo.nota.informado.valor.range}")
    private Integer nota;
}
