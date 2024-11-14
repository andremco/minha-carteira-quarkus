package org.finance.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import org.finance.models.data.Aporte;

import java.util.List;

@ApplicationScoped
public class AporteRepository implements PanacheRepository<Aporte> {
    public List<Aporte> findAcoesPaged(Integer pagina, Integer tamanho){
        return findAll(Sort.by("dataRegistroCriacao").descending()).page(Page.of(pagina, tamanho)).list();
    }
}
