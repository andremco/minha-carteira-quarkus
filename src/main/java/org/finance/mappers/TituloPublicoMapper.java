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
    @Mapping(target = "precoInicial", expression = "java(Formatter.doubleToReal(tituloPublico.getPrecoInicial()))")
    @Mapping(target = "valorRendimento", expression = "java(Formatter.doubleToReal(tituloPublico.getValorRendimento() != null ? tituloPublico.getValorRendimento() : 0))")
    @Mapping(target = "quantidade", expression = "java(AporteService.calcularQuantidadeCompras(tituloPublico.getAportes()))")
    TituloPublicoResponse toTituloPublicoResponse(TituloPublico tituloPublico, Setor setor);

    @Mapping(target = "dataRegistro", source = "tituloPublico.dataRegistroCriacao")
    @Mapping(target = "precoInicial", expression = "java(Formatter.doubleToReal(tituloPublico.getPrecoInicial()))")
    @Mapping(target = "valorRendimento", expression = "java(Formatter.doubleToReal(tituloPublico.getValorRendimento() != null ? tituloPublico.getValorRendimento() : 0))")
    @Mapping(target = "quantidade", expression = "java(AporteService.calcularQuantidadeCompras(tituloPublico.getAportes()))")
    TituloPublicoResponse toTituloPublicoResponse(TituloPublico tituloPublico);

    @Mapping(target = "descricao", source = "tituloPublico.descricao")
    @Mapping(target = "dataRegistro", source = "tituloPublico.dataRegistroCriacao")
    @Mapping(target = "precoInicial", expression = "java(Formatter.doubleToReal(tituloPublico.getPrecoInicial()))")
    @Mapping(target = "valorRendimento", expression = "java(Formatter.doubleToReal(tituloPublico.getValorRendimento() != null ? tituloPublico.getValorRendimento() : 0))")
    @Mapping(target = "quantidade", expression = "java(AporteService.calcularQuantidadeCompras(tituloPublico.getAportes()))")
    TituloPublicoResponse toTituloPublicoResponse(TituloPublico tituloPublico, String precoMedio, String valorTotalAtivo, String comprarOuAguardar, String lucroOuPerda);

    @Mapping(target = "descricao", source = "tituloPublico.descricao")
    @Mapping(target = "dataRegistro", source = "tituloPublico.dataRegistroCriacao")
    @Mapping(target = "precoInicial", expression = "java(Formatter.doubleToReal(tituloPublico.getPrecoInicial()))")
    @Mapping(target = "valorRendimento", expression = "java(Formatter.doubleToReal(tituloPublico.getValorRendimento() != null ? tituloPublico.getValorRendimento() : 0))")
    @Mapping(target = "quantidade", expression = "java(AporteService.calcularQuantidadeCompras(tituloPublico.getAportes()))")
    DetalharTituloPublicoResponse toDetalharTituloPublicoResponse(TituloPublico tituloPublico, String precoMedio,
                                                         String carteiraIdealPorcento, String carteiraTenhoPorcento,
                                                         String valorTotalCompras, String valorTotalVendas,
                                                         String valorTotalAtivo, String quantoQueroTotal, String quantoFaltaTotal,
                                                         Integer quantidadeQueFaltaTotal, String comprarOuAguardar, String lucroOuPerda);

    List<TituloPublicoResponse> toTitulosPublicoResponse(List<TituloPublico> titulos);
}
