package org.finance.models.enums;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.HashMap;

public enum TipoAtivoEnum {
    ACAO(1, "Ação"),
    FUNDO_IMOBILIARIO(2, "Fundo Imobiliário"),
    BRAZILIAN_DEPOSITARY_RECEIPTS(3, "Brazilian Depositary Receipts"),
    TITULO_PUBLICO(4, "Título Público");
    private static HashMap<Integer, TipoAtivoEnum> enumById = new HashMap<>();
    static {
        Arrays.stream(values()).forEach(e -> enumById.put(e.getId(), e));
    }

    public static TipoAtivoEnum getById(int id) {
        return enumById.getOrDefault(id, TipoAtivoEnum.ACAO);
    }

    @Getter
    @Setter
    private int id;
    @Getter
    @Setter
    private String descricao;

    TipoAtivoEnum(int id, String descricao) {
        this.id = id;
        this.descricao= descricao;
    }

    TipoAtivoEnum(int id) {
        this.id = id;
    }
}
