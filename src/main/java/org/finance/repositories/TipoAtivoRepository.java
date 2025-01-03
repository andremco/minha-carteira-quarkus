package org.finance.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.finance.models.data.TipoAtivo;

@ApplicationScoped
public class TipoAtivoRepository implements PanacheRepository<TipoAtivo> {
}
