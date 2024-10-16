package org.finance.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import org.finance.models.data.Acao;
import org.finance.models.data.Setor;

import java.util.List;

@ApplicationScoped
public class AcaoRepository implements PanacheRepository<Acao> {
    public Acao findByRazaoSocial(String razaoSocial){
        return find("razaoSocial", razaoSocial).firstResult();
    }
    public Acao findByTicker(String ticker){
        return find("ticker", ticker).firstResult();
    }
    public List<Acao> findAcoesPaged(Integer pagina, Integer tamanho){
        return findAll(Sort.by("dataRegistroCriacao").descending()).page(Page.of(pagina, tamanho)).list();
    }
}
