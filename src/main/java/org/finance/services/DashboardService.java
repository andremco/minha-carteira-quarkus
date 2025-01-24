package org.finance.services;

import com.arjuna.ats.jta.exceptions.NotImplementedException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.finance.configs.ApiConfigProperty;
import org.finance.exceptions.NegocioException;
import org.finance.mappers.DashboardMapper;
import org.finance.models.response.dashboard.AportesMensalResponse;
import org.finance.models.response.dashboard.AportesTotalResponse;
import org.finance.models.response.dashboard.ValoresCarteiraResponse;
import org.finance.repositories.mariadb.DashboardRepository;
import org.finance.utils.CalculosCarteira;
import org.finance.utils.Formatter;

import java.time.LocalDateTime;
import java.time.YearMonth;

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
        var anoVigente = fim.getYear();
        var ultimoMesPeriodo = fim.getMonth();

        if (inicio.isAfter(fim))
            throw new NegocioException(apiConfigProperty.getCampoDataInicioSuperiorDataFim());

        if (anoPeriodoInicial != anoVigente)
            throw new NegocioException(apiConfigProperty.getAnoPeriodoInformadoNaoPermitido());

        for (int mesPercorrido = ultimoMesPeriodo.getValue(); mesPercorrido != 0; mesPercorrido--){
            inicio = LocalDateTime.of(anoVigente, mesPercorrido, 1, 0, 0);
            YearMonth yearMonth = YearMonth.of(anoVigente, mesPercorrido);
            int ultimoDiaMes = yearMonth.lengthOfMonth();
            fim = LocalDateTime.of(anoVigente, mesPercorrido, ultimoDiaMes, 23, 59);
            //TODO montar o response
            var aportes = dashboardRepository.obterAportesTotal(inicio, fim);
        }

        throw new NotImplementedException();
    }

    public AportesTotalResponse obterAportesTotal() throws NotImplementedException {
        throw new NotImplementedException();
    }
}
