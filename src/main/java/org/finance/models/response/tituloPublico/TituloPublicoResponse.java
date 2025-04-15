package org.finance.models.response.tituloPublico;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.finance.models.response.dominio.DominioResponse;
import org.finance.models.response.setor.SetorResponse;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class TituloPublicoResponse {
    private Integer id;
    private SetorResponse setor;
    private String descricao;
    private double precoMedio;
    private double valorTotalAtivo;
    private double valorTotalAtivoAtual;
    private double precoInicial;
    private Double valorRendimento;
    private Integer nota;
    private Integer quantidade;
    private double lucroOuPerda;
    private String comprarOuAguardar;
    private String dataRegistro;
}
