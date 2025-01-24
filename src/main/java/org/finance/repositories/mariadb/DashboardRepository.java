package org.finance.repositories.mariadb;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.time.LocalDateTime;

@ApplicationScoped
public class DashboardRepository {
    @PersistenceContext
    EntityManager entityManager;
    private StringBuilder montarQueryAportesTotal(LocalDateTime dataInicio, LocalDateTime dataFim){
        String sql = """
                select
                    CARTEIRA_1.TOTAL_ACAO,
                    CARTEIRA_2.TOTAL_FIIS,
                    CARTEIRA_3.TOTAL_BDR,
                    CARTEIRA_4.TOTAL_TITULO
                from
                    (select if(sum(aporte.Preco*aporte.Quantidade) is not null,
                               sum(aporte.Preco*aporte.Quantidade), 0) AS TOTAL_ACAO,
                         aporte.DataRegistroCriacao AS DATA
                     from Aporte aporte
                        inner join Acao acao ON  aporte.AcaoId = acao.Id
                        inner join Setor setor ON acao.SetorId = setor.Id
                        inner join TipoAtivo tipo ON setor.TipoAtivoId = tipo.Id
                        where
                            1=1
                            and aporte.DataRegistroRemocao is null
                            #PERIODOINICIAL#
                            #PERIODOFIM#
                            and tipo.Id = 1 -- Ação
                    ) CARTEIRA_1,
                    (select if(sum(aporte.Preco*aporte.Quantidade) is not null,
                               sum(aporte.Preco*aporte.Quantidade), 0) AS TOTAL_FIIS
                     from Aporte aporte
                        inner join Acao acao ON  aporte.AcaoId = acao.Id
                        inner join Setor setor ON acao.SetorId = setor.Id
                        inner join TipoAtivo tipo ON setor.TipoAtivoId = tipo.Id
                        where
                            1=1
                            and aporte.DataRegistroRemocao is null
                            #PERIODOINICIAL#
                            #PERIODOFIM#
                            and tipo.Id = 2 -- Fundo Imobiliário
                    ) CARTEIRA_2,
                    (select if(sum(aporte.Preco*aporte.Quantidade) is not null,
                               sum(aporte.Preco*aporte.Quantidade), 0) AS TOTAL_BDR
                     from Aporte aporte
                        inner join Acao acao ON  aporte.AcaoId = acao.Id
                        inner join Setor setor ON acao.SetorId = setor.Id
                        inner join TipoAtivo tipo ON setor.TipoAtivoId = tipo.Id
                        where
                            1=1
                            and aporte.DataRegistroRemocao is null
                            #PERIODOINICIAL#
                            #PERIODOFIM#
                            and tipo.Id = 3 -- Brazilian Depositary Receipts
                    ) CARTEIRA_3,
                    (select if(sum(aporte.Preco*aporte.Quantidade) is not null,
                               sum(aporte.Preco*aporte.Quantidade), 0) AS TOTAL_TITULO
                     from Aporte aporte
                        inner join TituloPublico titulo ON  aporte.TituloPublicoId = titulo.Id
                        inner join Setor setor ON titulo.SetorId = setor.Id
                        inner join TipoAtivo tipo ON setor.TipoAtivoId = tipo.Id
                        where
                            1=1
                            and aporte.DataRegistroRemocao is null
                            #PERIODOINICIAL#
                            #PERIODOFIM#
                            and tipo.Id = 4 -- Título Público
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

    public Object obterAportesTotal(LocalDateTime dataInicio, LocalDateTime dataFim){
        StringBuilder query = montarQueryAportesTotal(dataInicio, dataFim);
        var result = entityManager.createNativeQuery(query.toString()).getResultList();
        return result;
    }
}
