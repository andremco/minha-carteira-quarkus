package org.finance.mappers;

import org.finance.models.data.mariadb.queries.AportesTotalPorTipoAtivo;
import org.finance.models.response.dashboard.*;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "jakarta-cdi", imports = {LocalDateTime.class})
public interface DashboardMapper {
    ValoresCarteiraResponse toValoresCarteiraResponse(String totalCarteira, String totalCarteiraAtualizado,
                                                      String lucroOuPerda, Boolean balancoPositivo);
    AportesValorMensalResponse toAportesValorMensalResponse(List<String> mesesPesquisados, List<AportesTipoAtivoMensalResponse> aportesAcoesMensal,
                                                       List<AportesTipoAtivoMensalResponse> aportesFIIsMensal, List<AportesTipoAtivoMensalResponse> aportesBDRsMensal,
                                                       List<AportesTipoAtivoMensalResponse> aportesTituloPublicoMensal);
    AportesValorTotalResponse toAportesValorTotalResponse(AportesTotalPorTipoAtivo aportes);
    AportesPorcetagemTotalResponse toAportesPorcetagemTotalResponse(Double porAcoes, Double porFIIs, Double porBDRs, Double porTitulos);
}
