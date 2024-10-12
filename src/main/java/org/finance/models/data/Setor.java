package org.finance.models.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@Entity(name = "Setor")
public class Setor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    private String descricao;
    private LocalDateTime dataRegistroCriacao;
    private LocalDateTime dataRegistroEdicao;
    @OneToMany(mappedBy = "setor")
    private List<Acao> acoes = new ArrayList<>();
}
