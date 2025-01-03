package org.finance.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import org.finance.models.data.Setor;

import java.util.List;

@ApplicationScoped
public class SetorRepository implements PanacheRepository<Setor> {
    public List<Setor> findByDescricao(String descricao){
        return find("descricao", descricao).list();
    }
    public List<Setor> findSetoresPaged(Integer pagina, Integer tamanho){
        return findAll(Sort.by("dataRegistroCriacao").descending()).page(Page.of(pagina, tamanho)).list();
    }
}

