package org.finance.models.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class ResponseApi<T> {
    private T data;
    private String message;
    private Boolean success;
}
