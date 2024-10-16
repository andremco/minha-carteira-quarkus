package org.finance.models.request.acao;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EditarAcaoRequest {
    @NotNull(message = "{campo.id.nao.informado}")
    private Integer id;
    private String razaoSocial;
    private Integer setorId;
    private String ticker;
    private Boolean ehFIIs;
    private Integer nota;
}
