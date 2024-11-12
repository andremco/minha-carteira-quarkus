package org.finance.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.finance.models.data.TituloPublico;

@ApplicationScoped
public class TituloPublicoRepository implements PanacheRepository<TituloPublico> {
}
