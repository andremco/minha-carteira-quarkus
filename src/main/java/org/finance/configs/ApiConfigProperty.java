package org.finance.configs;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
@Getter
public class ApiConfigProperty {
    @ConfigProperty(name = "registro.nao.encontrado")
    String registroNaoEncontrado;
    @ConfigProperty(name = "registro.ja.existe")
    String registroJaExiste;
    @ConfigProperty(name = "setor.nao.pode.ser.excluido")
    String setorNaoPodeSerExcluido;
    @ConfigProperty(name = "aporte.params.insuficiente")
    String aporteParmasInsuficiente;
    @ConfigProperty(name = "aporte.venda.nao.permitida")
    String aporteVendaNaoPermitida;
    @ConfigProperty(name = "acao.nao.pode.ser.excluido")
    String acaoNaoPodeSerExcluido;
    @ConfigProperty(name = "titulo.nao.pode.ser.excluido")
    String tituloNaoPodeSerExcluido;
}
