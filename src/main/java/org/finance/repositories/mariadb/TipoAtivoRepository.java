package org.finance.repositories.mariadb;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.finance.models.data.mariadb.entities.TipoAtivo;

@ApplicationScoped
public class TipoAtivoRepository implements PanacheRepository<TipoAtivo> {
}
