package org.finance.utils;

import jakarta.enterprise.context.ApplicationScoped;
import org.finance.models.data.mariadb.entities.Acao;
import org.finance.models.data.mariadb.entities.Aporte;
import org.finance.models.data.mariadb.entities.TituloPublico;
import org.finance.models.data.mariadb.queries.SetoresTotalNotas;
import org.finance.repositories.mariadb.AcaoRepository;
import org.finance.repositories.mariadb.TituloPublicoRepository;

import java.util.List;

@ApplicationScoped
public class CalculosCarteira {
    public double calcularCarteiraIdealQuociente(Integer nota, Integer somaTodasNotasCarteira){
        var carteiraIdeal = ((double)nota/somaTodasNotasCarteira);
        return Math.round(carteiraIdeal * 100.0) / 100.0;
    }

    public double calcularCarteiraTenhoQuociente(double valorTotalAtivo, double totalCarteira){
        if(valorTotalAtivo == 0)
            return 0;
        var carteiraTenho = (valorTotalAtivo/totalCarteira);
        return Math.round(carteiraTenho * 100.0) / 100.0;
    }

    public double calcularQuantoQuero(double carteiraIdeal, double totalCarteira){
        return carteiraIdeal*totalCarteira;
    }

    public double calcularQuantoFalta(double quantoQuero, double valorTotalAtivo){
        var quantoFalta = quantoQuero-valorTotalAtivo;
        return quantoFalta >= 0 ? quantoFalta : 0;
    }

    public double calcularQuantidadeQueFalta(double quantoFaltaTotal, double preco){
        double quantidadeQueFalta = quantoFaltaTotal/preco;
        return quantidadeQueFalta >= 0 ? quantidadeQueFalta : 0;
    }

    public double calcularLucroOuPerda(double valorTotalAtivo, double valorTotalAtivoAtual){
        return valorTotalAtivoAtual-valorTotalAtivo;
    }

    public double calcularPrecoMedioAportes(List<Aporte> aportes){
        if (aportes == null || aportes.isEmpty())
            return 0;
        var quantidadeAportes = aportes.stream().filter(a -> a.getMovimentacao() == 'C' && a.getDataRegistroRemocao() == null).toList().size();
        var somaValores = aportes.stream().filter(a -> a.getMovimentacao() == 'C' && a.getDataRegistroRemocao() == null).mapToDouble(Aporte::getPreco).sum();
        return somaValores/quantidadeAportes;
    }

    public String informarComprarOuAguardar(int quantidadeQueFaltaTotal){
        return quantidadeQueFaltaTotal > 0 ? "Comprar" : "Aguardar";
    }

    public int somarNotasPorSetores(List<SetoresTotalNotas> setores) {
        int total = 0;
        if (setores != null && !setores.isEmpty())
            total = setores.stream().mapToInt(SetoresTotalNotas::getTotalNotasAtivos).sum();

        return total;
    }
}
