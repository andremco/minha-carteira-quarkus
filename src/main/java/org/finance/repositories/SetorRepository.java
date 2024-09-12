package org.finance.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.finance.models.data.Setor;

@ApplicationScoped
public class SetorRepository implements PanacheRepository<Setor> {}
