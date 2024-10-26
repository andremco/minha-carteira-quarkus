package org.finance.models.request.acao;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SalvarAcaoRequest {
    @NotBlank(message = "{campo.razao.social.nao.informado}")
    @Size(max = 100, message = "{campo.razao.social.informado.chars.limite}")
    private String razaoSocial;
    @NotNull(message = "{campo.setor.id.nao.informado}")
    private Integer setorId;
    @NotNull(message = "campo.categoria.id.nao.informado")
    private Integer categoriaId;
    @NotBlank(message = "{campo.ticker.nao.informado}")
    @Size(max = 10, message = "{campo.ticker.informado.chars.limite}")
    private String ticker;
    @NotNull(message = "{campo.nota.nao.informado}")
    @Min(value = 0, message = "{campo.nota.informado.valor.range}")
    @Max(value = 10, message = "{campo.nota.informado.valor.range}")
    private Integer nota;
}
