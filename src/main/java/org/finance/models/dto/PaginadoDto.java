package org.finance.models.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class PaginadoDto<T> {
    private Integer pagina;
    private Integer tamanho;
    private Long total;
    private List<T> itens;
}
