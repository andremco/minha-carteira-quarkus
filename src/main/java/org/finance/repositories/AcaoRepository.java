package org.finance.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import org.finance.models.data.Acao;
import org.finance.models.data.Setor;

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
    public List<Acao> findAcoesPaged(String razaoSocial, Integer pagina, Integer tamanho){
        StringBuilder query = new StringBuilder("1=1 and dataRegistroRemocao is null");
        Map<String, Object> params = new HashMap<>();

        if (razaoSocial != null) {
            query.append(" and razaoSocial like :razaoSocial");
            params.put("razaoSocial", "%" + razaoSocial + "%");
        }
        return find(query.toString(), Sort.by("dataRegistroCriacao").descending(), params).page(Page.of(pagina, tamanho)).list();
    }
}
