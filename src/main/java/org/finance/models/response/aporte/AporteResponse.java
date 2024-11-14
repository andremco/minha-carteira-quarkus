package org.finance.models.response.aporte;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class AporteResponse {
    private Integer id;
    private Integer acaoId;
    private String acaoRazaoSocial;
    private Integer tituloPublicoId;
    private String tituloPublicoDescricao;
    private double preco;
    private String ticker;
    private Integer nota;
    private String dataRegistro;
}
