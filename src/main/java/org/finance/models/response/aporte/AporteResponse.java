package org.finance.models.response.aporte;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.finance.models.data.Acao;
import org.finance.models.data.TituloPublico;
import org.finance.models.response.acao.AcaoResponse;
import org.finance.models.response.tituloPublico.TituloPublicoResponse;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class AporteResponse {
    private Integer id;
    private AcaoResponse acao;
    private TituloPublicoResponse tituloPublico;
    private String preco;
    private Integer quantidade;
    private String movimentacao;
    private String dataRegistro;
}
