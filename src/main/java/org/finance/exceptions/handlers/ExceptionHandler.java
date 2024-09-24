package org.finance.exceptions.handlers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.finance.models.response.ResponseApi;

@Provider
public class ExceptionHandler implements ExceptionMapper<Exception> {
    @Override
    public Response toResponse(Exception e) {
        var response = new ResponseApi<>(null, new String[] { e.getMessage() }, false);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
    }
}
