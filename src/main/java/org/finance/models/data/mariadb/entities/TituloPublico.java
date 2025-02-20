package org.finance.models.data.mariadb.entities;

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
@Entity(name = "TituloPublico")
public class TituloPublico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    private String descricao;
    @NotNull
    private double precoInicial;
    private Double valorRendimento;
    @NotNull
    private Integer nota;
    @NotNull
    private LocalDateTime dataRegistroCriacao;
    private LocalDateTime dataRegistroEdicao;
    private LocalDateTime dataRegistroRemocao;
    @ManyToOne
    @JoinColumn(name = "SetorId")
    private Setor setor;
    @OneToMany(mappedBy = "tituloPublico")
    private List<Aporte> aportes = new ArrayList<>();
}
