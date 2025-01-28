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
@Entity(name = "TipoAtivo")
public class TipoAtivo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    private String descricao;
    @NotNull
    private LocalDateTime dataRegistroCriacao;
    @OneToMany(mappedBy = "tipoAtivo")
    private List<Setor> setores = new ArrayList<>();
}
