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

@Mapper(componentModel = "jakarta-cdi", imports = {LocalDateTime.class, AporteService.class}, uses = {SetorMapper.class, CategoriaMapper.class})
public interface AcaoMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "aportes", ignore = true)
    @Mapping(target = "dataRegistroCriacao", expression = "java(LocalDateTime.now())")
    @Mapping(target = "dataRegistroEdicao", ignore = true)
    @Mapping(target = "dataRegistroRemocao", ignore = true)
    Acao toAcao(SalvarAcaoRequest request, Setor setor, Categoria categoria);

    @Mapping(target = "id", source = "acao.id")
    @Mapping(target = "dataRegistro", source = "acao.dataRegistroCriacao")
    @Mapping(target = "quantidade", source = "quantidadeCompras")
    AcaoResponse toAcaoResponse(Acao acao, Setor setor, Categoria categoria, Integer quantidadeCompras);

    @Mapping(target = "dataRegistro", source = "acao.dataRegistroCriacao")
    @Mapping(target = "quantidade", expression = "java(AporteService.calcularQuantidadeCompras(acao.getAportes()))")
    AcaoResponse toAcaoResponse(Acao acao);

    @Mapping(target = "razaoSocial", source = "acao.razaoSocial")
    @Mapping(target = "ticker", source = "acao.ticker")
    @Mapping(target = "dataRegistro", source = "acao.dataRegistroCriacao")
    @Mapping(target = "quantidade", source = "quantidadeCompras")
    DetalharAcaoResponse toDetalharAcaoResponse(Acao acao, String precoDinamico, Integer quantidadeCompras,
                                                String carteiraIdealPorcento, String carteiraTenhoPorcento,
                                                String valorTotalAtivo, String valorTotalAtivoAtual,
                                                String quantoQueroTotal, String quantoFaltaTotal, Integer quantidadeQueFaltaTotal,
                                                String comprarOuAguardar, String lucroOuPerda);

    List<AcaoResponse> toAcoesResponse(List<Acao> acoes);
}
