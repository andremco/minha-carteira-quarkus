package org.finance.resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.finance.models.response.ResponseApi;
import org.finance.models.response.coin.CoinResponse;
import org.finance.services.CoinService;

@Path("/coin")
@Consumes(MediaType.APPLICATION_JSON)
public class CoinResource {
    @Inject
    CoinService service;
    @ConfigProperty(name = "operacao.realizado.com.sucesso")
    String operacaoSucesso;
    @GET
    @Path("/{coin}")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseApi<CoinResponse> obter(@PathParam("coin") String coin) {
        return new ResponseApi<>(service.obter(coin), new String[] {operacaoSucesso}, true);
    }
}
