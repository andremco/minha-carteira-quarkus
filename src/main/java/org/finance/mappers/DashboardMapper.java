package org.finance.mappers;

import org.finance.models.data.mariadb.queries.AportesTotalPorTipoAtivo;
import org.finance.models.data.mariadb.queries.SetoresFatiados;
import org.finance.models.response.dashboard.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "jakarta-cdi", imports = {LocalDateTime.class})
public interface DashboardMapper {
    ValoresCarteiraResponse toValoresCarteiraResponse(String totalCarteira, String totalCarteiraAtualizado,
                                                      String lucroOuPerda, Boolean balancoPositivo);
    AportesValorMensalResponse toAportesValorMensalResponse(List<String> mesesPesquisados, List<AportesTipoAtivoMensalResponse> aportesAcoesMensal,
                                                       List<AportesTipoAtivoMensalResponse> aportesFIIsMensal, List<AportesTipoAtivoMensalResponse> aportesBDRsMensal,
                                                       List<AportesTipoAtivoMensalResponse> aportesTituloPublicoMensal);
    AportesTotalResponse toAportesTotalResponse(BigDecimal porAcoes, BigDecimal porFIIs, BigDecimal porBDRs, BigDecimal porTitulos);

    @Mapping(target = "porAcoes", source = "totalAcoes")
    @Mapping(target = "porFIIs", source = "totalFIIs")
    @Mapping(target = "porBDRs", source = "totalBDRs")
    @Mapping(target = "porTitulos", source = "totalTitulos")
    AportesTotalResponse toAportesTotalResponse(AportesTotalPorTipoAtivo aportes);

    SetoresFatiadoResponse toSetoresFatiadoResponse(SetoresFatiados setores);

    List<SetoresFatiadoResponse> toSetoresFatiadoResponse(List<SetoresFatiados> setores);
}
