package org.finance.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.finance.models.data.Acao;

@ApplicationScoped
public class AcaoRepository implements PanacheRepository<Acao> {
    public Acao findByRazaoSocial(String razaoSocial){
        return find("razaoSocial", razaoSocial).firstResult();
    }
    public Acao findByTicker(String ticker){
        return find("ticker", ticker).firstResult();
    }
}
