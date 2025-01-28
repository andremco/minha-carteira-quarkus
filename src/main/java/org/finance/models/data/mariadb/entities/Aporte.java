package org.finance.models.data.mariadb.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "Aporte")
public class Aporte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    private double preco;
    @NotNull
    private Integer quantidade;
    @NotNull
    private char movimentacao;
    @NotNull
    private LocalDateTime dataRegistroCriacao;
    private LocalDateTime dataRegistroEdicao;
    private LocalDateTime dataRegistroRemocao;
    @ManyToOne
    @JoinColumn(name = "AcaoId")
    private Acao acao;
    @ManyToOne
    @JoinColumn(name = "TituloPublicoId")
    private TituloPublico tituloPublico;
}
