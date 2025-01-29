package org.finance.repositories.mariadb;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.finance.models.data.mariadb.queries.AportesTotalPorTipoAtivo;

import java.time.LocalDateTime;

@ApplicationScoped
public class DashboardRepository {
    @PersistenceContext
    EntityManager entityManager;
    private StringBuilder montarQueryAportesTotal(LocalDateTime dataInicio, LocalDateTime dataFim){
        String sql = """
            SELECT
                (CARTEIRA_COMPRA_1.TOTAL-CARTEIRA_VENDA_1.TOTAL) AS TOTAL_ACOES,
                (CARTEIRA_COMPRA_2.TOTAL-CARTEIRA_VENDA_2.TOTAL) AS TOTAL_FIIS,
                (CARTEIRA_COMPRA_3.TOTAL-CARTEIRA_VENDA_3.TOTAL) AS TOTAL_BDRS,
                (CARTEIRA_COMPRA_4.TOTAL-CARTEIRA_VENDA_4.TOTAL) AS TOTAL_TITULOS
                FROM
                    (SELECT IF(SUM(aporte.Preco*aporte.Quantidade) IS NOT NULL,
                               SUM(aporte.Preco*aporte.Quantidade), 0) AS TOTAL
                    FROM Aporte aporte
                    INNER JOIN Acao acao ON  aporte.AcaoId = acao.Id
                    INNER JOIN Setor setor ON acao.SetorId = setor.Id
                    INNER JOIN TipoAtivo tipo ON setor.TipoAtivoId = tipo.Id
                    WHERE
                        1=1
                        AND aporte.DataRegistroRemocao IS NULL
                        AND aporte.movimentacao = 'C'
                        #PERIODOINICIAL#
                        #PERIODOFIM#
                        AND tipo.Id = 1 -- Ação
                    ) CARTEIRA_COMPRA_1,
                    (SELECT IF(SUM(aporte.Preco*aporte.Quantidade) IS NOT NULL,
                               SUM(aporte.Preco*aporte.Quantidade), 0) AS TOTAL
                    FROM Aporte aporte
                    INNER JOIN Acao acao ON  aporte.AcaoId = acao.Id
                    INNER JOIN Setor setor ON acao.SetorId = setor.Id
                    INNER JOIN TipoAtivo tipo ON setor.TipoAtivoId = tipo.Id
                    WHERE
                        1=1
                        AND aporte.DataRegistroRemocao IS NULL
                        and aporte.movimentacao = 'V'
                        #PERIODOINICIAL#
                        #PERIODOFIM#
                        AND tipo.Id = 1 -- Ação
                    ) CARTEIRA_VENDA_1,
                    (SELECT IF(SUM(aporte.Preco*aporte.Quantidade) IS NOT NULL,
                               SUM(aporte.Preco*aporte.Quantidade), 0) AS TOTAL
                    FROM Aporte aporte
                    INNER JOIN Acao acao ON  aporte.AcaoId = acao.Id
                    INNER JOIN Setor setor ON acao.SetorId = setor.Id
                    INNER JOIN TipoAtivo tipo ON setor.TipoAtivoId = tipo.Id
                    WHERE
                        1=1
                        AND aporte.DataRegistroRemocao IS NULL
                        AND aporte.movimentacao = 'C'
                        #PERIODOINICIAL#
                        #PERIODOFIM#
                        AND tipo.Id = 2 -- Fundo Imobiliário
                    ) CARTEIRA_COMPRA_2,
                    (SELECT IF(SUM(aporte.Preco*aporte.Quantidade) IS NOT NULL,
                               SUM(aporte.Preco*aporte.Quantidade), 0) AS TOTAL
                    FROM Aporte aporte
                    INNER JOIN Acao acao ON  aporte.AcaoId = acao.Id
                    INNER JOIN Setor setor ON acao.SetorId = setor.Id
                    INNER JOIN TipoAtivo tipo ON setor.TipoAtivoId = tipo.Id
                    WHERE
                        1=1
                        AND aporte.DataRegistroRemocao IS NULL
                        AND aporte.movimentacao = 'V'
                        #PERIODOINICIAL#
                        #PERIODOFIM#
                        AND tipo.Id = 2 -- Fundo Imobiliário
                    ) CARTEIRA_VENDA_2,
                    (SELECT IF(SUM(aporte.Preco*aporte.Quantidade) IS NOT NULL,
                               SUM(aporte.Preco*aporte.Quantidade), 0) AS TOTAL
                    FROM Aporte aporte
                    INNER JOIN Acao acao ON  aporte.AcaoId = acao.Id
                    INNER JOIN Setor setor ON acao.SetorId = setor.Id
                    INNER JOIN TipoAtivo tipo ON setor.TipoAtivoId = tipo.Id
                    WHERE
                        1=1
                        AND aporte.DataRegistroRemocao IS NULL
                        AND aporte.movimentacao = 'C'
                        #PERIODOINICIAL#
                        #PERIODOFIM#
                        AND tipo.Id = 3 -- Brazilian Depositary Receipts
                    ) CARTEIRA_COMPRA_3,
                    (SELECT IF(SUM(aporte.Preco*aporte.Quantidade) IS NOT NULL,
                               SUM(aporte.Preco*aporte.Quantidade), 0) AS TOTAL
                    FROM Aporte aporte
                    INNER JOIN Acao acao ON  aporte.AcaoId = acao.Id
                    INNER JOIN Setor setor ON acao.SetorId = setor.Id
                    INNER JOIN TipoAtivo tipo ON setor.TipoAtivoId = tipo.Id
                    WHERE
                        1=1
                        AND aporte.DataRegistroRemocao IS NULL
                        AND aporte.movimentacao = 'V'
                        #PERIODOINICIAL#
                        #PERIODOFIM#
                        AND tipo.Id = 3 -- Brazilian Depositary Receipts
                    ) CARTEIRA_VENDA_3,
                    (SELECT IF(SUM(aporte.Preco*aporte.Quantidade) IS NOT NULL,
                               SUM(aporte.Preco*aporte.Quantidade), 0) AS TOTAL
                    FROM Aporte aporte
                    INNER JOIN TituloPublico titulo ON  aporte.TituloPublicoId = titulo.Id
                    INNER JOIN Setor setor ON titulo.SetorId = setor.Id
                    INNER JOIN TipoAtivo tipo ON setor.TipoAtivoId = tipo.Id
                    WHERE
                        1=1
                        AND aporte.DataRegistroRemocao IS NULL
                        AND aporte.movimentacao = 'C'
                        #PERIODOINICIAL#
                        #PERIODOFIM#
                        AND tipo.Id = 4 -- Título Público
                    ) CARTEIRA_COMPRA_4,
                    (SELECT IF(SUM(aporte.Preco*aporte.Quantidade) IS NOT NULL,
                               SUM(aporte.Preco*aporte.Quantidade), 0) AS TOTAL
                    FROM Aporte aporte
                    INNER JOIN TituloPublico titulo ON  aporte.TituloPublicoId = titulo.Id
                    INNER JOIN Setor setor ON titulo.SetorId = setor.Id
                    INNER JOIN TipoAtivo tipo ON setor.TipoAtivoId = tipo.Id
                    WHERE
                        1=1
                        AND aporte.DataRegistroRemocao IS NULL
                        AND aporte.movimentacao = 'V'
                        #PERIODOINICIAL#
                        #PERIODOFIM#
                        AND tipo.Id = 4 -- Título Público
                    ) CARTEIRA_VENDA_4              
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

    public AportesTotalPorTipoAtivo obterAportesTotal(LocalDateTime dataInicio, LocalDateTime dataFim){
        StringBuilder query = montarQueryAportesTotal(dataInicio, dataFim);
        var result = entityManager.createNativeQuery(query.toString(), AportesTotalPorTipoAtivo.class).getResultList();
        return (AportesTotalPorTipoAtivo)result.getFirst();
    }
}
