package org.finance.repositories.mariadb;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.finance.models.data.mariadb.entities.Setor;
import org.finance.models.data.mariadb.queries.SetoresTotalNotas;
import org.finance.models.enums.TipoAtivoEnum;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class SetorRepository implements PanacheRepository<Setor> {
    @PersistenceContext
    EntityManager entityManager;
    public List<Setor> findByDescricao(String descricao){
        return find("descricao", descricao).list();
    }
    public List<Setor> findSetoresPaged(Integer pagina, Integer tamanho){
        return findAll(Sort.by("dataRegistroCriacao").descending()).page(Page.of(pagina, tamanho)).list();
    }
    private StringBuilder montarQuerySetoresTotalNotas(TipoAtivoEnum tipoAtivo) {
        String sql = """
            SELECT
                setor.Descricao AS SETOR,
                IFNULL(SUM(ativo.Nota), 0) AS TOTAL_NOTAS_ATIVOS
            FROM #TABELA# ativo
            INNER JOIN Setor setor ON ativo.SetorId = setor.Id
            WHERE
                1=1
                AND ativo.DataRegistroRemocao IS NULL
                AND setor.ID in (SELECT ID FROM Setor WHERE TipoAtivoId = #TIPOATIVO#)
            GROUP BY setor.Descricao                
                """;
        if (tipoAtivo != TipoAtivoEnum.TITULO_PUBLICO)
            sql = sql.replace("#TABELA#", "Acao");
        if (tipoAtivo == TipoAtivoEnum.TITULO_PUBLICO)
            sql = sql.replace("#TABELA#", "TituloPublico");
        sql = sql.replace("#TIPOATIVO#", Integer.toString(tipoAtivo.getId()));
        return new StringBuilder(sql);
    }
    public List<SetoresTotalNotas> obterSetoresTotalNotas(TipoAtivoEnum tipoAtivo){
        StringBuilder query = montarQuerySetoresTotalNotas(tipoAtivo);
        Query result = entityManager.createNativeQuery(query.toString(), Object[].class);
        List<Object[]> setores = result.getResultList();

        return setores.stream()
                .map(obj -> SetoresTotalNotas
                        .builder()
                        .setor((String) obj[0])
                        .totalNotasAtivos(((BigDecimal) obj[1]).intValue())
                        .build())
                .collect(Collectors.toList());
    }
}

