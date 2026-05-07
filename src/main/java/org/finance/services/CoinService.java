package org.finance.services;

import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.finance.integration.currency.AwesomeClient;
import org.finance.mappers.CoinMapper;
import org.finance.models.data.mongo.Coin;
import org.finance.models.response.coin.CoinResponse;
import org.finance.repositories.mongo.CoinRepository;

import java.time.LocalDateTime;
import java.util.Comparator;

@ApplicationScoped
public class CoinService {
    @Inject
    @RestClient
    AwesomeClient awesomeClient;
    @Inject
    CoinRepository coinRepository;
    @Inject
    CoinMapper coinMapper;

    @CacheResult(cacheName = "buscar-coin")
    public CoinResponse obter(String coin){
        try{
            if (coin == null)
                return null;
            //Verifica se existe no mongo cotações já pesquisadas com diferença de um dia!
            var dataFiltro = LocalDateTime.now().minusDays(1)
                    .withHour(23).withMinute(59).withSecond(59).withNano(0);
            coin = coin.toUpperCase();
            CoinResponse response = obterCoinMongo(coin, dataFiltro);
            if (response != null)
                return response;
            var requestAwesomeApi = coin.toUpperCase() + "-BRL";
            var mapCoinResponse = awesomeClient.get(requestAwesomeApi);
            var keyMapCoin = coin+"BRL";
            if (mapCoinResponse != null && !mapCoinResponse.isEmpty() && mapCoinResponse.containsKey(keyMapCoin)){
                var mongoCoin = coinMapper.toCoinMongoResponse(mapCoinResponse.get(keyMapCoin));
                coinRepository.persist(mongoCoin);
                response = coinMapper.toCoinResponse(mongoCoin);
                return response;
            }
        }
        catch (WebApplicationException e){
            if (e.getResponse().getStatus() == 404 || e.getResponse().getStatus() == 402 || e.getResponse().getStatus() == 401){
                //Verifica se existe no mongo cotações já pesquisadas com diferença de 7 dias!
                var dataFiltro = LocalDateTime.now().minusDays(7)
                        .withHour(23).withMinute(59).withSecond(59).withNano(0);
                coin = coin.toUpperCase();
                CoinResponse response = obterCoinMongo(coin, dataFiltro);
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

    private CoinResponse obterCoinMongo(String coin, LocalDateTime data){
        var coins = coinRepository.findCoinByDate(coin, data);
        if (!coins.isEmpty()){
            coins.sort(Comparator.comparing(Coin::getQuotationDate).reversed());
            var mongoCoin = coins.getFirst();
            return coinMapper.toCoinResponse(mongoCoin);
        }
        return null;
    }
}
