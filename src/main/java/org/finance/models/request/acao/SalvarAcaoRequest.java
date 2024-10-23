package org.finance.models.request.acao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SalvarAcaoRequest {
    @NotBlank(message = "{campo.razao.social.nao.informado}")
    private String razaoSocial;
    @NotNull(message = "{campo.setor.id.nao.informado}")
    private Integer setorId;
    @NotNull(message = "campo.categoria.id.nao.informado")
    private Integer categoriaId;
    @NotBlank(message = "{campo.ticker.nao.informado}")
    private String ticker;
    @NotNull(message = "{campo.nota.nao.informado}")
    private Integer nota;
}
