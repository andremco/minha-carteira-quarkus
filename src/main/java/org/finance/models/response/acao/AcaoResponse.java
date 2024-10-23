package org.finance.models.response.acao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class AcaoResponse {
    private Integer id;
    private Integer setorId;
    private String setorDescricao;
    private Integer categoriaId;
    private String categoriaDescricao;
    private String razaoSocial;
    private String ticker;
    private Integer nota;
    private String dataRegistroCriacao;
}
