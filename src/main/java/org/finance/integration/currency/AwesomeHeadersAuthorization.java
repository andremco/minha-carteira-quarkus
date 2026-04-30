package org.finance.integration.currency;

import jakarta.ws.rs.core.MultivaluedMap;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.ext.ClientHeadersFactory;

public class AwesomeHeadersAuthorization implements ClientHeadersFactory {
    @ConfigProperty(name = "org.finance.integration.currency.AwesomeClient/mp-rest/header/x-api-key")
    String xApiKey;

    @Override
    public MultivaluedMap<String, String> update(MultivaluedMap<String, String> incomingHeaders, MultivaluedMap<String, String> outgoingHeaders) {
        outgoingHeaders.add("Authorization", xApiKey);
        return outgoingHeaders;
    }
}
