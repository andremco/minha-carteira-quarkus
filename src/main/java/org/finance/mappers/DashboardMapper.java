package org.finance.mappers;

import org.finance.models.data.mariadb.queries.AportesTotalPorTipoAtivo;
import org.finance.models.response.dashboard.AportesMensalResponse;
import org.finance.models.response.dashboard.AportesTipoAtivoMensalResponse;
import org.finance.models.response.dashboard.AportesTotalResponse;
import org.finance.models.response.dashboard.ValoresCarteiraResponse;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "jakarta-cdi", imports = {LocalDateTime.class})
public interface DashboardMapper {
    ValoresCarteiraResponse toValoresCarteiraResponse(String totalCarteira, String totalCarteiraAtualizado,
                                                      String lucroOuPerda, Boolean balancoPositivo);
    AportesMensalResponse toAportesMensalResponse(List<String> mesesPesquisados, List<AportesTipoAtivoMensalResponse> aportesAcoesMensal,
                                                  List<AportesTipoAtivoMensalResponse> aportesFIIsMensal, List<AportesTipoAtivoMensalResponse> aportesBDRsMensal,
                                                  List<AportesTipoAtivoMensalResponse> aportesTituloPublicoMensal);
    AportesTotalResponse toAportesTotalResponse(AportesTotalPorTipoAtivo aportes);
}
