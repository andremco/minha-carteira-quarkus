package org.finance.models.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class Paginado<T> {
    private Integer pagina;
    private Integer tamanho;
    private Long total;
    private List<T> itens;
}
