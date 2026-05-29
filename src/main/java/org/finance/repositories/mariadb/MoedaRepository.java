package org.finance.repositories.mariadb;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import org.finance.models.data.mariadb.entities.Moeda;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class MoedaRepository implements PanacheRepository<Moeda> {
    public Moeda findByNome(String nome){
        return find("nome", nome).firstResult();
    }

    public Moeda findByCodigo(String codigo){
        return find("codigo", codigo).firstResult();
    }

    private StringBuilder montarQueryBuilder(String descricaoAtivo){
        StringBuilder query = new StringBuilder("1=1 and dataRegistroRemocao is null");
        if (descricaoAtivo != null) {
            query.append(" and (nome like :nome or codigo like :codigo)");
        }
        return query;
    }

    private Map<String, Object> montarParamsQuery(String descricaoAtivo){
        Map<String, Object> params = new HashMap<>();
        if (descricaoAtivo != null) {
            params.put("nome", "%" + descricaoAtivo + "%");
            params.put("codigo", "%" + descricaoAtivo + "%");
        }
        return params;
    }

    public List<Moeda> findMoedasPaged(String descricaoAtivo, Integer pagina, Integer tamanho){
        StringBuilder query = montarQueryBuilder(descricaoAtivo);
        Map<String, Object> params = montarParamsQuery(descricaoAtivo);
        return find(query.toString(), Sort.by("nome").ascending(), params).page(Page.of(pagina, tamanho)).list();
    }

    public long total(String descricaoAtivo){
        StringBuilder query = montarQueryBuilder(descricaoAtivo);
        Map<String, Object> params = montarParamsQuery(descricaoAtivo);
        return find(query.toString(), params).count();
    }
}
