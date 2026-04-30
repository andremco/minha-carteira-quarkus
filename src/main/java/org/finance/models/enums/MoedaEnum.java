package org.finance.models.enums;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.HashMap;

public enum MoedaEnum {
    DOLAR(1, "Dólar"),
    BITCOIN(2, "Bitcoin");

    @Getter
    @Setter
    private int id;
    @Getter
    @Setter
    private String descricao;

    private static HashMap<Integer, MoedaEnum> enumById = new HashMap<>();
    static {
        Arrays.stream(values()).forEach(e -> enumById.put(e.getId(), e));
    }

    public static MoedaEnum getById(int id) {
        return enumById.getOrDefault(id, MoedaEnum.DOLAR);
    }

    MoedaEnum(int id, String descricao) {
        this.id = id;
        this.descricao= descricao;
    }

    MoedaEnum(int id) {
        this.id = id;
    }
}
