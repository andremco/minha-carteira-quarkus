package org.finance.models.response.coin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CoinResponse {
    public String nome;
    public String codigo;
    public double precoDinamico;
    private String dataCotacao;
}
