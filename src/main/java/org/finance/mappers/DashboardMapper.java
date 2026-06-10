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
    ValoresCarteiraResponse toValoresCarteiraResponse(double totalCarteira, double totalCarteiraAtualizado,
                                                      double lucroOuPerda, Boolean balancoPositivo);
    AportesValorMensalResponse toAportesValorMensalResponse(List<String> mesesPesquisados, List<AportesTipoAtivoMensalResponse> aportesAcoesMensal,
                                                       List<AportesTipoAtivoMensalResponse> aportesFIIsMensal, List<AportesTipoAtivoMensalResponse> aportesBDRsMensal,
                                                       List<AportesTipoAtivoMensalResponse> aportesTituloPublicoMensal, List<AportesTipoAtivoMensalResponse> aportesMoedasMensal);
    AportesTotalResponse toAportesTotalResponse(BigDecimal porAcoes, BigDecimal porFIIs, BigDecimal porBDRs, BigDecimal porTitulos, BigDecimal porMoedas);

    @Mapping(target = "porAcoes", source = "totalAcoes")
    @Mapping(target = "porFIIs", source = "totalFIIs")
    @Mapping(target = "porBDRs", source = "totalBDRs")
    @Mapping(target = "porTitulos", source = "totalTitulos")
    @Mapping(target = "porMoedas", source = "totalMoedas")
    AportesTotalResponse toAportesTotalResponse(AportesTotalPorTipoAtivo aportes);

    @Mapping(target = "percentual", expression = "java(setores.getTotalAportado().doubleValue())")
    SetoresFatiadoResponse toSetoresFatiadoResponse(SetoresFatiados setores);

    SetoresFatiadoResponse toSetoresFatiadoResponse(String setor, Double percentual);

    List<SetoresFatiadoResponse> toSetoresFatiadoResponse(List<SetoresFatiados> setores);
}
