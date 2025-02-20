package org.finance.services;

import io.quarkus.cache.CacheResult;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.GenericType;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.finance.configs.ApiConfigProperty;
import org.finance.integration.market.BrapiClient;
import org.finance.mappers.TickerMapper;
import org.finance.models.data.mongo.Ticker;
import org.finance.models.dto.integration.response.GetQuoteResponse;
import org.finance.models.response.ticker.TickerResponse;
import org.finance.repositories.mongo.TickerRepository;

import java.time.LocalDateTime;
import java.util.Comparator;

@ApplicationScoped
public class TickerService {
    @Inject
    @RestClient
    BrapiClient apiClient;
    @Inject
    TickerRepository tickerRepository;
    @Inject
    TickerMapper tickerMapper;
    @Inject
    ApiConfigProperty apiConfigProperty;

    @CacheResult(cacheName = "buscar-ticker")
    public TickerResponse obter(String ticker){
        try{
            //Verifica se existe no mongo cotações já pesquisadas com diferença de um dia!
            var dataFiltro = LocalDateTime.now().minusDays(1)
                    .withHour(23).withMinute(59).withSecond(59).withNano(0);
            TickerResponse response = obterTickerMongo(ticker, dataFiltro);
            if (response != null)
                return response;
            var quote = apiClient.getQuote(ticker);
            if (quote != null && quote.getResults() != null && !quote.getResults().isEmpty()){
                var apiTicker = quote.getResults().getFirst();
                var mongoTicker = tickerMapper.toTickerMongoResponse(apiTicker);
                tickerRepository.persist(mongoTicker);
                response = tickerMapper.toTickerResponse(apiTicker);
                return response;
            }
        }
        catch (WebApplicationException e){
            if (e.getResponse().getStatus() == 404 || e.getResponse().getStatus() == 402){
                //Verifica se existe no mongo cotações já pesquisadas com diferença de 7 dias!
                var dataFiltro = LocalDateTime.now().minusDays(7)
                    .withHour(23).withMinute(59).withSecond(59).withNano(0);
                TickerResponse response = obterTickerMongo(ticker, dataFiltro);
                if (response != null)
                    return response;
            }
            throw e;
        }
        catch (Exception e){
            throw e;
        }
        return null;
    }

    public TickerResponse buscar(String ticker){
        var quote = apiClient.listQuotes(ticker);
        if (quote != null && quote.getStocks() != null && !quote.getStocks().isEmpty())
            return tickerMapper.toTickerResponse(quote.getStocks().getFirst());
        return null;
    }

    private TickerResponse obterTickerMongo(String ticker, LocalDateTime data){
        var tickers = tickerRepository.findTickerByDate(ticker, data);
        if (!tickers.isEmpty()){
            tickers.sort(Comparator.comparing(Ticker::getDataCotacao).reversed());
            var mongoTicker = tickers.getFirst();
            return tickerMapper.toTickerResponse(mongoTicker);
        }
        return null;
    }
}
