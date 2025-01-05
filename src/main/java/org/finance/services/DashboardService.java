package org.finance.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.finance.mappers.DashboardMapper;
import org.finance.models.response.dashboard.ValoresCarteiraResponse;
import org.finance.utils.Formatter;

@ApplicationScoped
public class DashboardService {
    @Inject
    AporteService aporteService;
    @Inject
    DashboardMapper mapper;
    public ValoresCarteiraResponse obterValoresCarteira(){
        var totalCarteira = Formatter.doubleToReal(aporteService.calcularTotalCarteira());
        var totalCarteiraAtualizado = Formatter.doubleToReal(aporteService.calcularTotalCarteiraAtualizado());

        return mapper.toValoresCarteiraResponse(totalCarteira, totalCarteiraAtualizado);
    }
}
