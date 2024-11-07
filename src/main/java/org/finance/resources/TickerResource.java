package org.finance.resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.finance.models.response.ResponseApi;
import org.finance.models.response.ticker.TickerResponse;
import org.finance.services.TickerService;

@Path("/ticker")
@Consumes(MediaType.APPLICATION_JSON)
public class TickerResource {
    @Inject
    TickerService service;
    @ConfigProperty(name = "operacao.realizado.com.sucesso")
    String operacaoSucesso;
    @GET
    @Path("/{ticker}")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseApi<TickerResponse> obter(@PathParam("ticker") String ticker) {
        return new ResponseApi<>(service.obter(ticker), new String[] {operacaoSucesso}, true);
    }
}
