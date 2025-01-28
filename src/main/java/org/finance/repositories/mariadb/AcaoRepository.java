package org.finance.repositories.mariadb;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import org.finance.models.data.mariadb.entities.Acao;
import org.finance.models.enums.TipoAtivoEnum;

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
    private StringBuilder montarQueryBuilder(TipoAtivoEnum tipoAtivo, String razaoSocial){
        StringBuilder query = new StringBuilder("1=1 and dataRegistroRemocao is null");
        if (tipoAtivo != null){
            query.append(" and setor.tipoAtivo.id = :tipoAtivoId");
        }
        if (razaoSocial != null) {
            query.append(" and razaoSocial like :razaoSocial");
        }
        return query;
    }
    private Map<String, Object> montarParamsQuery(TipoAtivoEnum tipoAtivo, String razaoSocial){
        Map<String, Object> params = new HashMap<>();
        if (tipoAtivo != null){
            params.put("tipoAtivoId", tipoAtivo.getId());
        }
        if (razaoSocial != null) {
            params.put("razaoSocial", "%" + razaoSocial + "%");
        }
        return params;
    }
    public List<Acao> findAcoesPaged(TipoAtivoEnum tipoAtivo, String razaoSocial, Integer pagina, Integer tamanho){
        StringBuilder query = montarQueryBuilder(tipoAtivo, razaoSocial);
        Map<String, Object> params = montarParamsQuery(tipoAtivo, razaoSocial);
        return find(query.toString(), Sort.by("razaoSocial").ascending(), params).page(Page.of(pagina, tamanho)).list();
    }
    public long total(TipoAtivoEnum tipoAtivo, String razaoSocial){
        StringBuilder query = montarQueryBuilder(tipoAtivo, razaoSocial);
        Map<String, Object> params = montarParamsQuery(tipoAtivo, razaoSocial);
        return find(query.toString(), params).count();
    }
}
