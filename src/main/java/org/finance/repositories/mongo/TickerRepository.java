package org.finance.repositories.mongo;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import io.quarkus.mongodb.panache.PanacheQuery;
import jakarta.enterprise.context.ApplicationScoped;
import org.bson.Document;
import org.finance.models.data.mongo.Ticker;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class TickerRepository implements PanacheMongoRepository<Ticker> {
    public List<Ticker> findTickerByDate(String ticker, LocalDateTime data){
        Document query = new Document()
                .append("ticker", ticker)
                .append("dataCotacao", new Document("$gte", data));

        // Usando o método find para buscar os documentos
        PanacheQuery<Ticker> tickerQuery = find(query);

        // Retornando a lista de resultados
        return tickerQuery.list();
    }
}
