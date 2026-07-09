package org.finance.models.request.aporte;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EditarAporteRequest {
    @NotNull(message = "{campo.id.nao.informado}")
    private Integer id;
    private Integer acaoId;
    private Integer tituloPublicoId;
    private Integer moedaId;
    @Positive(message = "{campo.preco.valor.invalido}")
    private Double preco;
    @Positive(message = "{campo.quantidade.valor.invalido}")
    private Double quantidade;
    @Pattern(regexp = "[CV]", message = "{campo.movimentacao.valor.invalido}")
    private String movimentacao;
}
