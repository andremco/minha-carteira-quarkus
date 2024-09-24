package org.finance.exceptions.handlers;

import io.quarkus.hibernate.validator.runtime.jaxrs.ResteasyReactiveViolationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.finance.models.response.ResponseApi;

import java.util.Objects;

@Provider
public class ViolationExceptionHandler implements ExceptionMapper<ResteasyReactiveViolationException> {
    @Override
    public Response toResponse(ResteasyReactiveViolationException e) {
        var response = new ResponseApi<>(null, null, false);
        if (e.getConstraintViolations() != null){
            String[] mensagens = e.getConstraintViolations()
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .filter(Objects::nonNull).toArray(String[]::new);
            response.setMessage(mensagens);
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
    }
}
