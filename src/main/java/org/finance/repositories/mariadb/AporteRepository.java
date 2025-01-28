package org.finance.repositories.mariadb;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import org.finance.models.data.mariadb.entities.Aporte;
import org.finance.models.enums.TipoAtivoEnum;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class AporteRepository implements PanacheRepository<Aporte> {
    private StringBuilder montarQueryBuilder(TipoAtivoEnum tipoAtivo, Integer ativoId, LocalDateTime dataInicio, LocalDateTime dataFim){
        StringBuilder query = new StringBuilder("1=1 and dataRegistroRemocao is null");
        if (tipoAtivo != null && ativoId == null){
            if (tipoAtivo == TipoAtivoEnum.ACAO){
                query.append(" and acao.id is not null");
            }
            if (tipoAtivo == TipoAtivoEnum.TITULO_PUBLICO){
                query.append(" and tituloPublico.id is not null");
            }
        }
        if (tipoAtivo != null && ativoId != null){
            if (tipoAtivo == TipoAtivoEnum.ACAO){
                query.append(" and acao.id = :acaoId");
            }
            if (tipoAtivo == TipoAtivoEnum.TITULO_PUBLICO){
                query.append(" and tituloPublico.id = :tituloPublicoId");
            }
        }
        if (dataInicio != null)
            query.append(" and dataRegistroCriacao >= :dataInicio");
        if (dataFim != null)
            query.append(" and dataRegistroCriacao <= :dataFim");
        return query;
    }
    private Map<String, Object> montarParamsQuery(TipoAtivoEnum tipoAtivo, Integer ativoId, LocalDateTime dataInicio, LocalDateTime dataFim){
        Map<String, Object> params = new HashMap<>();
        if (tipoAtivo != null && ativoId != null){
            if (tipoAtivo == TipoAtivoEnum.ACAO){
                params.put("acaoId", ativoId);
            }
            if (tipoAtivo == TipoAtivoEnum.TITULO_PUBLICO){
                params.put("tituloPublicoId", ativoId);
            }
        }
        if (dataInicio != null)
            params.put("dataInicio", dataInicio);
        if (dataFim != null)
            params.put("dataFim", dataFim);
        return params;
    }
    public List<Aporte> findAcoesPaged(TipoAtivoEnum tipoAtivo, Integer ativoId, LocalDateTime dataInicio, LocalDateTime dataFim, Integer pagina, Integer tamanho){
        StringBuilder query = montarQueryBuilder(tipoAtivo, ativoId, dataInicio, dataFim);
        Map<String, Object> params = montarParamsQuery(tipoAtivo, ativoId, dataInicio, dataFim);
        return find(query.toString(), Sort.by("dataRegistroCriacao").descending(), params).page(Page.of(pagina, tamanho)).list();
    }

    public long total(TipoAtivoEnum tipoAtivo, Integer ativoId, LocalDateTime dataInicio, LocalDateTime dataFim){
        StringBuilder query = montarQueryBuilder(tipoAtivo, ativoId, dataInicio, dataFim);
        Map<String, Object> params = montarParamsQuery(tipoAtivo, ativoId, dataInicio, dataFim);
        return find(query.toString(), params).count();
    }
}
