package org.finance.exceptions;

import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class NegocioException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 1L;
    private Response.Status status;
    public NegocioException(String message) {
        super(message);
    }

    public NegocioException(String message, Response.Status status){
        super(message);
        this.status = status;
    }

    public NegocioException(String message, Throwable cause) {
        super(message, cause);
    }

    public NegocioException(Throwable cause) {
        super(cause);
    }

    public NegocioException(String message, Throwable cause,
                           boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
