package org.finance.services;

import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.finance.integration.currency.AwesomeClient;
import org.finance.models.response.coin.CoinResponse;
import org.finance.models.response.ticker.TickerResponse;

import java.time.LocalDateTime;

@ApplicationScoped
public class CoinService {
    @Inject
    @RestClient
    AwesomeClient awesomeClient;

    @CacheResult(cacheName = "buscar-coin")
    public CoinResponse obter(String coin){
        try{
            var mapCoinResponse = awesomeClient.get(coin);
            if (mapCoinResponse != null && !mapCoinResponse.isEmpty() && mapCoinResponse.containsKey(coin)){
                //TODO aqui
            }
        }
        catch (WebApplicationException e){
            /*if (e.getResponse().getStatus() == 404 || e.getResponse().getStatus() == 402 || e.getResponse().getStatus() == 401){
                //Verifica se existe no mongo cotações já pesquisadas com diferença de 7 dias!
                var dataFiltro = LocalDateTime.now().minusDays(7)
                        .withHour(23).withMinute(59).withSecond(59).withNano(0);
                if (response != null)
                    return response;
            }*/
            throw e;
        }
        catch (Exception e){
            throw e;
        }
        return null;
    }
}
