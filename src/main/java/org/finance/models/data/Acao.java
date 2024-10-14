package org.finance.models.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "Acao")
public class Acao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    private String razaoSocial;
    @NotNull
    private String ticker;
    @NotNull
    private Boolean ehFIIs;
    @NotNull
    private Integer nota;
    @NotNull
    private LocalDateTime dataRegistroCriacao;
    private LocalDateTime dataRegistroEdicao;
    private LocalDateTime dataRegistroRemocao;
    @ManyToOne
    @JoinColumn(name = "SetorId")
    private Setor setor;
}
