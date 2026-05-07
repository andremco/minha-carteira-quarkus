package org.finance.repositories.mongo;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import io.quarkus.mongodb.panache.PanacheQuery;
import jakarta.enterprise.context.ApplicationScoped;
import org.bson.Document;
import org.finance.models.data.mongo.Coin;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class CoinRepository implements PanacheMongoRepository<Coin> {
    public List<Coin> findCoinByDate(String code, LocalDateTime data){
        Document query = new Document()
                .append("code", code)
                .append("quotationDate", new Document("$gte", data));

        // Usando o método find para buscar os documentos
        PanacheQuery<Coin> tickerQuery = find(query);

        // Retornando a lista de resultados
        return tickerQuery.list();
    }
}
