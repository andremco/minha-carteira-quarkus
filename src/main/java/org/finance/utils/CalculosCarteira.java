package org.finance.utils;

import jakarta.enterprise.context.ApplicationScoped;
import org.finance.models.data.mariadb.Acao;
import org.finance.models.data.mariadb.Aporte;
import org.finance.models.data.mariadb.TituloPublico;
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

    public int somarNotasCarteira(AcaoRepository acaoRepository, TituloPublicoRepository tituloPublicoRepository){
        var notasAcao = acaoRepository.findAll().stream().mapToInt(Acao::getNota).sum();
        var notasTitulos = tituloPublicoRepository.findAll().stream().mapToInt(TituloPublico::getNota).sum();

        return notasAcao + notasTitulos;
    }
}
