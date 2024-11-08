package org.finance.integration.market;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.finance.models.dto.integration.response.GetQuoteResponse;
import org.finance.models.dto.integration.response.ListQuoteResponse;

@Path("/quote")
@RegisterRestClient
@Produces(MediaType.APPLICATION_JSON)
@RegisterClientHeaders(BrapiHeadersAuthorization.class)
public interface BrapiClient {
    @GET
    @Path("/{ticker}")
    GetQuoteResponse getQuote(@PathParam("ticker") String ticker);

    @GET
    @Path("/list")
    ListQuoteResponse listQuotes(@QueryParam("search") String ticker);
}
