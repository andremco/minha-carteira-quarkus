package org.finance.services;

import com.arjuna.ats.jta.exceptions.NotImplementedException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.finance.configs.ApiConfigProperty;
import org.finance.exceptions.NegocioException;
import org.finance.mappers.DashboardMapper;
import org.finance.models.response.dashboard.AportesMensalResponse;
import org.finance.models.response.dashboard.AportesTipoAtivoMensalResponse;
import org.finance.models.response.dashboard.AportesTotalResponse;
import org.finance.models.response.dashboard.ValoresCarteiraResponse;
import org.finance.repositories.mariadb.DashboardRepository;
import org.finance.utils.CalculosCarteira;
import org.finance.utils.Formatter;

import java.text.DateFormatSymbols;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

@ApplicationScoped
public class DashboardService {
    @Inject
    DashboardRepository dashboardRepository;
    @Inject
    AporteService aporteService;
    @Inject
    CalculosCarteira calculosCarteira;
    @Inject
    DashboardMapper mapper;
    @Inject
    ApiConfigProperty apiConfigProperty;
    public ValoresCarteiraResponse obterValoresCarteira(){
        var totalCarteira = aporteService.calcularTotalCarteira();
        var totalCarteiraAtualizado = aporteService.calcularTotalCarteiraAtualizado();
        var lucroOuPerda = calculosCarteira.calcularLucroOuPerda(totalCarteira, totalCarteiraAtualizado);
        var balancoPositivo = lucroOuPerda >= 0;

        return mapper.toValoresCarteiraResponse(Formatter.doubleToReal(totalCarteira),
                Formatter.doubleToReal(totalCarteiraAtualizado),
                Formatter.doubleToReal(lucroOuPerda),
                balancoPositivo);
    }

    public AportesMensalResponse obterAportesMensal(String dataInicio, String dataFim) throws NotImplementedException {
        LocalDateTime inicio = Formatter.stringToLocalDateTime(dataInicio);
        LocalDateTime fim = Formatter.stringToLocalDateTime(dataFim).withHour(23).withMinute(59);

        var anoPeriodoInicial = inicio.getYear();
        var primeiroMesPeriodo = inicio.getMonth();
        var anoVigente = fim.getYear();
        var ultimoMesPeriodo = fim.getMonth();

        if (inicio.isAfter(fim))
            throw new NegocioException(apiConfigProperty.getCampoDataInicioSuperiorDataFim());

        if (anoPeriodoInicial != anoVigente)
            throw new NegocioException(apiConfigProperty.getAnoPeriodoInformadoNaoPermitido());

        String[] nomeMesesCurtos = new DateFormatSymbols(Locale.of("pt", "BR")).getShortMonths();
        if (nomeMesesCurtos != null && nomeMesesCurtos.length > 0){
            nomeMesesCurtos = Arrays.stream(nomeMesesCurtos).map(m -> {
                var mes = m.toUpperCase();
                mes = mes.replace(".", "");
                return mes;
            }).toArray(String[]::new);
        }

        var mesesPesquisados = new ArrayList<String>();
        var aportesAcoesMensal = new ArrayList<AportesTipoAtivoMensalResponse>();
        var aportesFIIsMensal = new ArrayList<AportesTipoAtivoMensalResponse>();
        var aportesBDRsMensal = new ArrayList<AportesTipoAtivoMensalResponse>();
        var aportesTituloPublicoMensal = new ArrayList<AportesTipoAtivoMensalResponse>();
        var response = AportesMensalResponse.builder().build();

        for (int mesPercorrido = primeiroMesPeriodo.getValue(); mesPercorrido <= ultimoMesPeriodo.getValue(); mesPercorrido++){
            YearMonth yearMonth = YearMonth.of(anoVigente, mesPercorrido);
            int ultimoDiaMes = yearMonth.lengthOfMonth();

            inicio = LocalDateTime.of(anoVigente, mesPercorrido, 1, 0, 0);
            fim = LocalDateTime.of(anoVigente, mesPercorrido, ultimoDiaMes, 23, 59);

            var aportes = dashboardRepository.obterAportesTotal(inicio, fim);

            if (aportes != null && nomeMesesCurtos != null){
                var mesPesquisado = nomeMesesCurtos[mesPercorrido-1];
                if (mesesPesquisados.stream().noneMatch(m -> m.equals(mesPesquisado)))
                    mesesPesquisados.add(mesPesquisado);
                aportesAcoesMensal.add(AportesTipoAtivoMensalResponse.builder()
                        .mes(mesPesquisado)
                        .totalAportado(aportes.getTotalAcoes())
                        .build());
                aportesFIIsMensal.add(AportesTipoAtivoMensalResponse.builder()
                        .mes(mesPesquisado)
                        .totalAportado(aportes.getTotalFIIs())
                        .build());
                aportesBDRsMensal.add(AportesTipoAtivoMensalResponse.builder()
                        .mes(mesPesquisado)
                        .totalAportado(aportes.getTotalBDRs())
                        .build());
                aportesTituloPublicoMensal.add(AportesTipoAtivoMensalResponse.builder()
                        .mes(mesPesquisado)
                        .totalAportado(aportes.getTotalTitulos())
                        .build());
            }
        }
        response = mapper.toAportesMensalResponse(mesesPesquisados, aportesAcoesMensal,
                aportesFIIsMensal, aportesBDRsMensal, aportesTituloPublicoMensal);
        return response;
    }

    public AportesTotalResponse obterAportesTotal(){
        var aportes = dashboardRepository.obterAportesTotal(null, null);
        return mapper.toAportesTotalResponse(aportes);
    }
}
