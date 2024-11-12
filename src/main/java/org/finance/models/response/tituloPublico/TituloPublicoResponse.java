package org.finance.models.response.tituloPublico;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class TituloPublicoResponse {
    private Integer id;
    private Integer setorId;
    private String setorDescricao;
    private String descricao;
    private double precoInicial;
    private Integer nota;
    private String dataRegistro;
}
