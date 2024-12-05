package org.finance.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import org.finance.models.data.Acao;
import org.finance.models.data.TituloPublico;

import java.util.List;

@ApplicationScoped
public class TituloPublicoRepository implements PanacheRepository<TituloPublico> {
    public List<TituloPublico> findTitulosPaged(Integer pagina, Integer tamanho){
        return find("dataRegistroRemocao is null", Sort.by("dataRegistroCriacao").descending()).page(Page.of(pagina, tamanho)).list();
    }
}
