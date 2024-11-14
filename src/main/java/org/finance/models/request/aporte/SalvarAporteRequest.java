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
public class SalvarAporteRequest {
    @NotNull(message = "{campo.acao.id.nao.informado}")
    private Integer acaoId;
    @NotNull(message = "{campo.titulo.publico.id.nao.informado}")
    private Integer tituloPublicoId;
    @NotNull(message = "{campo.preco.nao.informado}")
    @Positive(message = "{campo.preco.valor.invalido}")
    private Double preco;
    @NotNull(message = "{campo.quantidade.nao.informado}")
    @Positive(message = "{campo.quantidade.valor.invalido}")
    private Integer quantidade;
    @NotNull(message = "{campo.movimentacao.nao.informado}")
    @Pattern(regexp = "[CV]", message = "{campo.movimentacao.valor.invalido}")
    private String movimentacao;
}
