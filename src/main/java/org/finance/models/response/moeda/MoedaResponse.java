package org.finance.models.response.moeda;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MoedaResponse {
    private Integer id;
    private String nome;
    private String codigo;
    private double precoDinamico;
    private String dataCotacao;
    private String dataRegistro;
}
