package org.finance.integration.currency;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.finance.models.dto.integration.response.currency.QuoteCurrencyResponse;

import java.util.Map;

@Path("/json/last")
@RegisterRestClient
@Produces(MediaType.APPLICATION_JSON)
@RegisterClientHeaders(AwesomeHeadersAuthorization.class)
public interface AwesomeClient {
    @GET
    @Path("/{price-coin}")
    Map<String, QuoteCurrencyResponse> get(@PathParam("price-coin") String priceCoin);
}
