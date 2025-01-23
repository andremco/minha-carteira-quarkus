package org.finance.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.finance.mappers.DashboardMapper;
import org.finance.models.response.dashboard.ValoresCarteiraResponse;
import org.finance.utils.CalculosCarteira;
import org.finance.utils.Formatter;

@ApplicationScoped
public class DashboardService {
    @Inject
    AporteService aporteService;
    @Inject
    CalculosCarteira calculosCarteira;
    @Inject
    DashboardMapper mapper;
    public ValoresCarteiraResponse obterValoresCarteira(){
        var totalCarteira = aporteService.calcularTotalCarteira();
        var totalCarteiraAtualizado = aporteService.calcularTotalCarteiraAtualizado();
        var lucroOuPerda = calculosCarteira.calcularLucroOuPerda(totalCarteira, totalCarteiraAtualizado);

        return mapper.toValoresCarteiraResponse(Formatter.doubleToReal(totalCarteira),
                Formatter.doubleToReal(totalCarteiraAtualizado),
                Formatter.doubleToReal(lucroOuPerda));
    }
}
