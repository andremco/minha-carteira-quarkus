package org.finance.utils;

import org.finance.models.data.mariadb.entities.Acao;
import org.finance.models.data.mariadb.entities.Moeda;
import org.finance.models.data.mariadb.entities.TituloPublico;

public class Util {
    public static String sinalizarCompraOuVenda(char movimentacao){
        return movimentacao == 'C' ? "Compra" : "Venda";
    }

    public static Integer selecionarIdAtivo(Acao acao, TituloPublico tituloPublico, Moeda moeda){
        if(acao != null)
            return acao.getId();
        if(tituloPublico != null)
            return tituloPublico.getId();
        if(moeda != null)
            return moeda.getId();
        return null;
    }
}
