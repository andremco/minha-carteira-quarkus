package org.finance.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import org.finance.models.data.Acao;
import org.finance.models.data.Setor;
import org.finance.models.enums.CategoriaEnum;
import org.finance.models.enums.TipoAtivoEnum;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class AcaoRepository implements PanacheRepository<Acao> {
    public Acao findByRazaoSocial(String razaoSocial){
        return find("razaoSocial", razaoSocial).firstResult();
    }
    public Acao findByTicker(String ticker){
        return find("ticker", ticker).firstResult();
    }
    private StringBuilder montarQueryBuilder(CategoriaEnum categoria, String razaoSocial){
        StringBuilder query = new StringBuilder("1=1 and dataRegistroRemocao is null");
        if (categoria != null){
            query.append(" and categoria.id = :categoriaId");
        }
        if (razaoSocial != null) {
            query.append(" and razaoSocial like :razaoSocial");
        }
        return query;
    }
    private Map<String, Object> montarParamsQuery(CategoriaEnum categoria, String razaoSocial){
        Map<String, Object> params = new HashMap<>();
        if (categoria != null){
            params.put("categoriaId", categoria.getId());
        }
        if (razaoSocial != null) {
            params.put("razaoSocial", razaoSocial);
        }
        return params;
    }
    public List<Acao> findAcoesPaged(CategoriaEnum categoria, String razaoSocial, Integer pagina, Integer tamanho){
        StringBuilder query = montarQueryBuilder(categoria, razaoSocial);
        Map<String, Object> params = montarParamsQuery(categoria, razaoSocial);
        return find(query.toString(), Sort.by("dataRegistroCriacao").descending(), params).page(Page.of(pagina, tamanho)).list();
    }
    public long total(CategoriaEnum categoria, String razaoSocial){
        StringBuilder query = montarQueryBuilder(categoria, razaoSocial);
        Map<String, Object> params = montarParamsQuery(categoria, razaoSocial);
        return find(query.toString(), params).count();
    }
}
