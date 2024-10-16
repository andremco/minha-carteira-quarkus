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
    @NotBlank(message = "{campo.ticker.nao.informado}")
    private String ticker;
    @NotNull(message = "{campo.eh.fiis.nao.informado}")
    private Boolean ehFIIs;
    @NotNull(message = "{campo.nota.nao.informado}")
    private Integer nota;
}
