package org.finance.exceptions.handlers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.finance.exceptions.NegocioException;
import org.finance.models.response.ResponseApi;

@Provider
public class NegocioExceptionHandler implements ExceptionMapper<NegocioException> {
    @Override
    public Response toResponse(NegocioException e) {
        return Response.status(Response.Status.BAD_REQUEST).
                entity(new ResponseApi<>(null, e.getMessage(), false)).build();
    }
}