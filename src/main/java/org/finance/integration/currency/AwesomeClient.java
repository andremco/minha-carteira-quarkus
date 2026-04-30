package org.finance.integration.currency;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/json/last")
@RegisterRestClient
@Produces(MediaType.APPLICATION_JSON)
@RegisterClientHeaders(AwesomeHeadersAuthorization.class)
public interface AwesomeClient {
    @GET
    Object get();
}
