package org.finance.models.response.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class AportesValorMensalResponse {
    private List<String> mesesPesquisados;
    private List<AportesTipoAtivoMensalResponse> aportesAcoesMensal;
    private List<AportesTipoAtivoMensalResponse> aportesFIIsMensal;
    private List<AportesTipoAtivoMensalResponse> aportesBDRsMensal;
    private List<AportesTipoAtivoMensalResponse> aportesTituloPublicoMensal;
}
