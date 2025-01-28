package org.finance.repositories.mariadb;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import org.finance.models.data.mariadb.entities.TituloPublico;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class TituloPublicoRepository implements PanacheRepository<TituloPublico> {
    public List<TituloPublico> findTitulosPaged(String descricao, Integer pagina, Integer tamanho){
        StringBuilder query = new StringBuilder("1=1 and dataRegistroRemocao is null");
        Map<String, Object> params = new HashMap<>();

        if (descricao != null) {
            query.append(" and descricao like :descricao");
            params.put("descricao", "%" + descricao + "%");
        }
        return find(query.toString(), Sort.by("descricao").ascending(), params).page(Page.of(pagina, tamanho)).list();
    }
}
