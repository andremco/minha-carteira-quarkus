package org.finance.models.response.moeda;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class MoedaResponse {
    public Integer id;
    public String nome;
    public String codigo;
    public String nota;
    private Integer quantidade;
    public double precoDinamico;
    private double lucroOuPerda;
    private String comprarOuAguardar;
    private double valorTotalAtivoAtual;
    private String dataRegistro;
}