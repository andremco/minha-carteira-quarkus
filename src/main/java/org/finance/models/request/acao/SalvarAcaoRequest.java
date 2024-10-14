package org.finance.models.request.acao;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SalvarAcaoRequest {
    @NotBlank(message = "{campo.razao.social.nao.informado}")
    private String razaoSocial;
    @NotBlank(message = "{campo.setor.id.nao.informado}")
    private Integer setorId;
    @NotBlank(message = "{campo.ticker.nao.informado}")
    private String ticker;
    @NotBlank(message = "{campo.eh.fiis.nao.informado}")
    private Boolean ehFIIs;
    @NotBlank(message = "{campo.nota.nao.informado}")
    private Integer nota;
}
