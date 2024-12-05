package org.finance.models.request.acao;

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
public class EditarAcaoRequest {
    @NotNull(message = "{campo.id.nao.informado}")
    private Integer id;
    @Size(max = 100, message = "{campo.razao.social.informado.chars.limite}")
    private String razaoSocial;
    private Integer setorId;
    private Integer categoriaId;
    @Size(max = 10, message = "{campo.ticker.informado.chars.limite}")
    private String ticker;
    @Min(value = 0, message = "{campo.nota.informado.valor.range}")
    @Max(value = 10, message = "{campo.nota.informado.valor.range}")
    private Integer nota;
}
