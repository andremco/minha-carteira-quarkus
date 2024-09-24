package org.finance.models.dto.setor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class SetorDto {
    private Integer id;
    private String descricao;
}
