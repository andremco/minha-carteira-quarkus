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

    private StringBuilder montarQueryBuilder(String descricaoMoeda){
        StringBuilder query = new StringBuilder("1=1 and dataRegistroRemocao is null");
        if (descricaoMoeda != null) {
            query.append(" and (nome like :nome or codigo like :codigo)");
        }
        return query;
    }

    private Map<String, Object> montarParamsQuery(String descricaoMoeda){
        Map<String, Object> params = new HashMap<>();
        if (descricaoMoeda != null) {
            params.put("nome", "%" + descricaoMoeda + "%");
            params.put("codigo", "%" + descricaoMoeda + "%");
        }
        return params;
    }

    public List<Moeda> findMoedasPaged(String descricaoMoeda, Integer pagina, Integer tamanho){
        StringBuilder query = montarQueryBuilder(descricaoMoeda);
        Map<String, Object> params = montarParamsQuery(descricaoMoeda);
        return find(query.toString(), Sort.by("nome").ascending(), params).page(Page.of(pagina, tamanho)).list();
    }

    public long total(String descricaoMoeda){
        StringBuilder query = montarQueryBuilder(descricaoMoeda);
        Map<String, Object> params = montarParamsQuery(descricaoMoeda);
        return find(query.toString(), params).count();
    }
}
