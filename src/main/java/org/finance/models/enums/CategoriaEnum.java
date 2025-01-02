package org.finance.models.enums;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.HashMap;

public enum CategoriaEnum {
    ACAO(1, "Ação"),
    FUNDO_IMOBILIARIO(2, "Fundo Imobiliário"),
    BRAZILIAN_DEPOSITARY_RECEIPTS(3, "Brazilian Depositary Receipts");
    private static HashMap<Integer, CategoriaEnum> enumById = new HashMap<>();
    static {
        Arrays.stream(values()).forEach(e -> enumById.put(e.getId(), e));
    }

    public static CategoriaEnum getById(int id) {
        return enumById.getOrDefault(id, CategoriaEnum.ACAO);
    }

    @Getter
    @Setter
    private int id;
    @Getter
    @Setter
    private String descricao;

    CategoriaEnum(int id, String descricao) {
        this.id = id;
        this.descricao= descricao;
    }

    CategoriaEnum(int id) {
        this.id = id;
    }
}
