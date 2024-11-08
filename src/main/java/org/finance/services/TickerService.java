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
import org.finance.models.dto.integration.response.GetQuoteResponse;
import org.finance.models.response.ticker.TickerResponse;

@ApplicationScoped
public class TickerService {
    @Inject
    @RestClient
    BrapiClient apiClient;
    @Inject
    TickerMapper tickerMapper;
    @Inject
    ApiConfigProperty apiConfigProperty;

    @CacheResult(cacheName = "obter")
    public TickerResponse obter(String ticker){
        try{
            var quote = apiClient.getQuote(ticker);
            if (quote != null && quote.getResults() != null && !quote.getResults().isEmpty())
                return tickerMapper.toTickerResponse(quote.getResults().getFirst());
        }
        catch (WebApplicationException e){
            if (e.getResponse().getStatus() == 404)
                return null;
            throw e;
        }
        return null;
    }

    @CacheResult(cacheName = "buscar")
    public TickerResponse buscar(String ticker){
        var quote = apiClient.listQuotes(ticker);
        if (quote != null && quote.getStocks() != null && !quote.getStocks().isEmpty())
            return tickerMapper.toTickerResponse(quote.getStocks().getFirst());
        return null;
    }
}
