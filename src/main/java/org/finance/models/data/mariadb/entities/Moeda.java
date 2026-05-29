package org.finance.models.data.mariadb.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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
    private LocalDateTime dataRegistroCriacao;
    private LocalDateTime dataRegistroEdicao;
    private LocalDateTime dataRegistroRemocao;
}
