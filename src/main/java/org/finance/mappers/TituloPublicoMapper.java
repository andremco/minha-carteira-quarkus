package org.finance.mappers;

import org.finance.models.data.mariadb.entities.Setor;
import org.finance.models.data.mariadb.entities.TituloPublico;
import org.finance.models.request.tituloPublico.SalvarTituloPublicoRequest;
import org.finance.models.response.tituloPublico.DetalharTituloPublicoResponse;
import org.finance.models.response.tituloPublico.TituloPublicoResponse;
import org.finance.services.AporteService;
import org.finance.utils.Formatter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "jakarta-cdi", imports = { LocalDateTime.class, AporteService.class, Formatter.class }, uses = {SetorMapper.class})
public interface TituloPublicoMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "descricao", source = "request.descricao")
    @Mapping(target = "dataRegistroCriacao", expression = "java(LocalDateTime.now())")
    @Mapping(target = "valorRendimento", ignore = true)
    @Mapping(target = "dataRegistroEdicao", ignore = true)
    @Mapping(target = "dataRegistroRemocao", ignore = true)
    @Mapping(target = "aportes", ignore = true)
    TituloPublico toTituloPublico(SalvarTituloPublicoRequest request, Setor setor);

    @Mapping(target = "id", source = "tituloPublico.id")
    @Mapping(target = "descricao", source = "tituloPublico.descricao")
    @Mapping(target = "dataRegistro", source = "tituloPublico.dataRegistroCriacao")
    @Mapping(target = "precoInicial", source = "tituloPublico.precoInicial")
    @Mapping(target = "valorRendimento", source = "tituloPublico.valorRendimento")
    @Mapping(target = "quantidade", ignore = true)
    @Mapping(target = "precoMedio", ignore = true)
    @Mapping(target = "lucroOuPerda", ignore = true)
    @Mapping(target = "comprarOuAguardar", ignore = true)
    @Mapping(target = "valorTotalAtivo", ignore = true)
    @Mapping(target = "valorTotalAtivoAtual", ignore = true)
    TituloPublicoResponse toTituloPublicoResponse(TituloPublico tituloPublico, Setor setor);

    @Mapping(target = "dataRegistro", source = "tituloPublico.dataRegistroCriacao")
    @Mapping(target = "precoInicial", source = "tituloPublico.precoInicial")
    @Mapping(target = "valorRendimento", source = "tituloPublico.valorRendimento")
    @Mapping(target = "quantidade", ignore = true)
    @Mapping(target = "precoMedio", ignore = true)
    @Mapping(target = "lucroOuPerda", ignore = true)
    @Mapping(target = "comprarOuAguardar", ignore = true)
    @Mapping(target = "valorTotalAtivo", ignore = true)
    @Mapping(target = "valorTotalAtivoAtual", ignore = true)
    TituloPublicoResponse toTituloPublicoResponse(TituloPublico tituloPublico);

    @Mapping(target = "descricao", source = "tituloPublico.descricao")
    @Mapping(target = "dataRegistro", source = "tituloPublico.dataRegistroCriacao")
    @Mapping(target = "precoInicial", source = "tituloPublico.precoInicial")
    @Mapping(target = "valorRendimento", source = "tituloPublico.valorRendimento")
    @Mapping(target = "quantidade", source = "quantidadeQueTenho")
    TituloPublicoResponse toTituloPublicoResponse(TituloPublico tituloPublico, double precoMedio,
                                                  double quantidadeQueTenho, double valorTotalAtivo, double valorTotalAtivoAtual,
                                                  String comprarOuAguardar, double lucroOuPerda);

    @Mapping(target = "descricao", source = "tituloPublico.descricao")
    @Mapping(target = "dataRegistro", source = "tituloPublico.dataRegistroCriacao")
    @Mapping(target = "precoInicial", source = "tituloPublico.precoInicial")
    @Mapping(target = "valorRendimento", source = "tituloPublico.valorRendimento")
    @Mapping(target = "quantidade", source = "quantidadeQueTenho")
    DetalharTituloPublicoResponse toDetalharTituloPublicoResponse(TituloPublico tituloPublico, double precoMedio,
                                                         double carteiraIdealPorcento, double carteiraTenhoPorcento,
                                                         double valorTotalCompras, double valorTotalVendas,
                                                         double valorTotalAtivo, double valorTotalAtivoAtual, double quantoQueroTotal, double quantoFaltaTotal,
                                                         double quantidadeQueTenho, double quantidadeQueFaltaTotal, String comprarOuAguardar, double lucroOuPerda);

    List<TituloPublicoResponse> toTitulosPublicoResponse(List<TituloPublico> titulos);
}
