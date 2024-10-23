package org.finance.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.finance.models.data.Categoria;
@ApplicationScoped
public class CategoriaRepository implements PanacheRepository<Categoria> {
}
