package org.finance.mappers;

import org.finance.models.data.Acao;
import org.finance.models.data.Categoria;
import org.finance.models.data.Setor;
import org.finance.models.request.acao.SalvarAcaoRequest;
import org.finance.models.response.acao.AcaoResponse;
import org.finance.models.response.acao.DetalharAcaoResponse;
import org.finance.models.response.ticker.TickerResponse;
import org.finance.services.AcaoService;
import org.finance.services.AporteService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "jakarta-cdi", imports = {LocalDateTime.class}, uses = {SetorMapper.class, CategoriaMapper.class})
public interface AcaoMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "aportes", ignore = true)
    @Mapping(target = "dataRegistroCriacao", expression = "java(LocalDateTime.now())")
    @Mapping(target = "dataRegistroEdicao", ignore = true)
    @Mapping(target = "dataRegistroRemocao", ignore = true)
    Acao toAcao(SalvarAcaoRequest request, Setor setor, Categoria categoria);

    @Mapping(target = "id", source = "acao.id")
    @Mapping(target = "dataRegistro", source = "acao.dataRegistroCriacao")
    @Mapping(target = "quantidade", source = "quantidadeAportes")
    AcaoResponse toAcaoResponse(Acao acao, Setor setor, Categoria categoria, Integer quantidadeAportes);

    @Mapping(target = "dataRegistro", source = "acao.dataRegistroCriacao")
    @Mapping(target = "quantidade", ignore = true)
    AcaoResponse toAcaoResponse(Acao acao);

    @Mapping(target = "razaoSocial", source = "acao.razaoSocial")
    @Mapping(target = "ticker", source = "acao.ticker")
    @Mapping(target = "dataRegistro", source = "acao.dataRegistroCriacao")
    @Mapping(target = "quantidade", source = "quantidadeAportes")
    DetalharAcaoResponse toDetalharAcaoResponse(Acao acao, Double precoDinamico, Integer quantidadeAportes,
                                                Double carteiraIdeal, Double carteiraTenho,
                                                Double tenhoTotalPorAportes, Double tenhoTotalPorPrecoAtual);

    List<AcaoResponse> toAcoesResponse(List<Acao> acoes);
}
