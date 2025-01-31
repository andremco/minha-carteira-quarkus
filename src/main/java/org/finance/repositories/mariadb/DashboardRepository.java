package org.finance.repositories.mariadb;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.finance.models.data.mariadb.queries.AportesTotalPorTipoAtivo;
import org.finance.models.data.mariadb.queries.SetoresFatiados;
import org.finance.models.data.mariadb.queries.SetoresTotalNotas;
import org.finance.models.enums.TipoAtivoEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class DashboardRepository {
    @PersistenceContext
    EntityManager entityManager;
    private StringBuilder montarQueryAportesTotal(LocalDateTime dataInicio, LocalDateTime dataFim){
        String sql = """
            SELECT
                CARTEIRA_1.TOTAL AS TOTAL_ACOES,
                CARTEIRA_2.TOTAL AS TOTAL_FIIS,
                CARTEIRA_3.TOTAL AS TOTAL_BDRS,
                CARTEIRA_4.TOTAL AS TOTAL_TITULOS
                FROM
                    (SELECT IFNULL(SUM(CASE When Movimentacao='C' Then Preco*Quantidade Else 0 End) -
                                   SUM(CASE When Movimentacao='V' Then Preco*Quantidade Else 0 End), 0) AS TOTAL
                    FROM Aporte aporte
                    INNER JOIN Acao acao ON  aporte.AcaoId = acao.Id
                    INNER JOIN Setor setor ON acao.SetorId = setor.Id
                    INNER JOIN TipoAtivo tipo ON setor.TipoAtivoId = tipo.Id
                    WHERE
                        1=1
                        AND aporte.DataRegistroRemocao IS NULL
                        #PERIODOINICIAL#
                        #PERIODOFIM#
                        AND tipo.Id = 1 -- Ação
                    ) CARTEIRA_1,
                    (SELECT IFNULL(SUM(CASE When Movimentacao='C' Then Preco*Quantidade Else 0 End) -
                                   SUM(CASE When Movimentacao='V' Then Preco*Quantidade Else 0 End), 0) AS TOTAL
                    FROM Aporte aporte
                    INNER JOIN Acao acao ON  aporte.AcaoId = acao.Id
                    INNER JOIN Setor setor ON acao.SetorId = setor.Id
                    INNER JOIN TipoAtivo tipo ON setor.TipoAtivoId = tipo.Id
                    WHERE
                        1=1
                        AND aporte.DataRegistroRemocao IS NULL
                        #PERIODOINICIAL#
                        #PERIODOFIM#
                        AND tipo.Id = 2 -- Fundo Imobiliário
                    ) CARTEIRA_2,
                    (SELECT IFNULL(SUM(CASE When Movimentacao='C' Then Preco*Quantidade Else 0 End) -
                                   SUM(CASE When Movimentacao='V' Then Preco*Quantidade Else 0 End), 0) AS TOTAL
                    FROM Aporte aporte
                    INNER JOIN Acao acao ON  aporte.AcaoId = acao.Id
                    INNER JOIN Setor setor ON acao.SetorId = setor.Id
                    INNER JOIN TipoAtivo tipo ON setor.TipoAtivoId = tipo.Id
                    WHERE
                        1=1
                        AND aporte.DataRegistroRemocao IS NULL
                        #PERIODOINICIAL#
                        #PERIODOFIM#
                        AND tipo.Id = 3 -- Brazilian Depositary Receipts
                    ) CARTEIRA_3,
                    (SELECT IFNULL(SUM(CASE When Movimentacao='C' Then Preco*Quantidade Else 0 End) -
                                   SUM(CASE When Movimentacao='V' Then Preco*Quantidade Else 0 End), 0) AS TOTAL
                    FROM Aporte aporte
                    INNER JOIN TituloPublico titulo ON  aporte.TituloPublicoId = titulo.Id
                    INNER JOIN Setor setor ON titulo.SetorId = setor.Id
                    INNER JOIN TipoAtivo tipo ON setor.TipoAtivoId = tipo.Id
                    WHERE
                        1=1
                        AND aporte.DataRegistroRemocao IS NULL
                        #PERIODOINICIAL#
                        #PERIODOFIM#
                        AND tipo.Id = 4 -- Título Público
                    ) CARTEIRA_4                      
                """;
        if (dataInicio != null && dataFim != null){
            sql = sql.replace("#PERIODOINICIAL#", " and aporte.DataRegistroCriacao >= '" + dataInicio + "'");
            sql = sql.replace("#PERIODOFIM#", " and aporte.DataRegistroCriacao <= '" + dataFim + "'");
        }
        else {
            sql = sql.replace("#PERIODOINICIAL#", "");
            sql = sql.replace("#PERIODOFIM#", "");
        }

        return new StringBuilder(sql);
    }

    private StringBuilder montarQuerySetoresFatiados(TipoAtivoEnum tipoAtivo){
        String sql = """
            SELECT
                setor.Descricao AS SETOR,
                IFNULL(SUM(CASE When Movimentacao='C' Then Preco*Quantidade Else 0 End) - 
                       SUM(CASE When Movimentacao='V' Then Preco*Quantidade Else 0 End), 0) AS TOTAL_APORTADO
            FROM Aporte aporte
            #JOIN#
            WHERE
                1=1
                AND aporte.DataRegistroRemocao IS NULL
                AND setor.ID in (SELECT ID FROM Setor WHERE TipoAtivoId = #TIPOATIVO#)
            GROUP BY setor.Descricao                
                """;
        if (tipoAtivo != TipoAtivoEnum.TITULO_PUBLICO)
            sql = sql.replace("#JOIN#", """
                    INNER JOIN Acao acao ON  aporte.AcaoId = acao.Id
                    INNER JOIN Setor setor ON acao.SetorId = setor.Id
                    """);
        if (tipoAtivo == TipoAtivoEnum.TITULO_PUBLICO)
            sql = sql.replace("#JOIN#", """
                    INNER JOIN TituloPublico titulo ON  aporte.TituloPublicoId = titulo.Id
                    INNER JOIN Setor setor ON titulo.SetorId = setor.Id
                    """);
        sql = sql.replace("#TIPOATIVO#", Integer.toString(tipoAtivo.getId()));
        return new StringBuilder(sql);
    }

    public AportesTotalPorTipoAtivo obterAportesTotal(LocalDateTime dataInicio, LocalDateTime dataFim){
        StringBuilder query = montarQueryAportesTotal(dataInicio, dataFim);
        var result = entityManager.createNativeQuery(query.toString(), AportesTotalPorTipoAtivo.class).getResultList();
        return (AportesTotalPorTipoAtivo)result.getFirst();
    }

    public List<SetoresFatiados> obterSetoresFatiados(TipoAtivoEnum tipoAtivo){
        StringBuilder query = montarQuerySetoresFatiados(tipoAtivo);
        Query result = entityManager.createNativeQuery(query.toString(), Object[].class);
        List<Object[]> setores = result.getResultList();

        return setores.stream()
                .map(obj -> SetoresFatiados
                        .builder()
                        .setor((String) obj[0])
                        .totalAportado((BigDecimal) obj[1])
                        .build())
                .collect(Collectors.toList());
    }
}
