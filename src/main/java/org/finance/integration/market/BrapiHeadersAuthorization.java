package org.finance.integration.market;

import jakarta.ws.rs.core.MultivaluedMap;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.ext.ClientHeadersFactory;

public class BrapiHeadersAuthorization implements ClientHeadersFactory {
    @ConfigProperty(name = "org.finance.integration.market.BrapiClient/mp-rest/header/Authorization")
    String bearerToken;

    @Override
    public MultivaluedMap<String, String> update(MultivaluedMap<String, String> incomingHeaders, MultivaluedMap<String, String> outgoingHeaders) {
        outgoingHeaders.add("Authorization", bearerToken);
        return outgoingHeaders;
    }
}
