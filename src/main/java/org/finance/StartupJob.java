package org.finance;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.finance.services.AporteService;
import org.finance.utils.Formatter;

@ApplicationScoped
public class StartupJob {
    @Inject
    AporteService aporteService;
    void onStart(@Observes StartupEvent ev) {
        var totalCarteira = aporteService.calcularTotalCarteira();
        var totalCarteiraAtualizado = aporteService.calcularTotalCarteiraAtualizado();
        System.out.println("Minha carteira: " + totalCarteira + " - Minha carteira (atualizado): " + totalCarteiraAtualizado);
    }
}
