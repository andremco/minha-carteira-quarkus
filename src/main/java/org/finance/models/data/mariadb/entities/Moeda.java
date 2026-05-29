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
@Entity(name = "Moeda")
public class Moeda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    private String nome;
    @NotNull
    private String codigo;
    @NotNull
    private Integer nota;
    @NotNull
    private LocalDateTime dataRegistroCriacao;
    private LocalDateTime dataRegistroEdicao;
    private LocalDateTime dataRegistroRemocao;
    @OneToMany(mappedBy = "moeda")
    private List<Aporte> aportes = new ArrayList<>();
}
