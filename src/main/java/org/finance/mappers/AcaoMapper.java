package org.finance.mappers;

import org.finance.models.data.mariadb.entities.Acao;
import org.finance.models.data.mariadb.entities.Setor;
import org.finance.models.request.acao.SalvarAcaoRequest;
import org.finance.models.response.acao.AcaoResponse;
import org.finance.models.response.acao.DetalharAcaoResponse;
import org.finance.services.AporteService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "jakarta-cdi", imports = {LocalDateTime.class, AporteService.class}, uses = {SetorMapper.class})
public interface AcaoMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "aportes", ignore = true)
    @Mapping(target = "dataRegistroCriacao", expression = "java(LocalDateTime.now())")
    @Mapping(target = "dataRegistroEdicao", ignore = true)
    @Mapping(target = "dataRegistroRemocao", ignore = true)
    Acao toAcao(SalvarAcaoRequest request, Setor setor);

    @Mapping(target = "id", source = "acao.id")
    @Mapping(target = "dataRegistro", source = "acao.dataRegistroCriacao")
    @Mapping(target = "quantidade", ignore = true)
    AcaoResponse toAcaoResponse(Acao acao, Setor setor);

    @Mapping(target = "dataRegistro", source = "acao.dataRegistroCriacao")
    @Mapping(target = "quantidade", ignore = true)
    AcaoResponse toAcaoResponse(Acao acao);

    @Mapping(target = "dataRegistro", source = "acao.dataRegistroCriacao")
    @Mapping(target = "quantidade", source = "quantidadeQueTenho")
    AcaoResponse toAcaoResponse(Acao acao, String precoDinamico, Integer quantidadeQueTenho, String valorTotalAtivoAtual, String comprarOuAguardar, String lucroOuPerda);

    @Mapping(target = "razaoSocial", source = "acao.razaoSocial")
    @Mapping(target = "ticker", source = "acao.ticker")
    @Mapping(target = "dataRegistro", source = "acao.dataRegistroCriacao")
    @Mapping(target = "quantidade", source = "quantidadeQueTenho")
    DetalharAcaoResponse toDetalharAcaoResponse(Acao acao, String precoDinamico,
                                                String carteiraIdealPorcento, String carteiraTenhoPorcento,
                                                String valorTotalAtivo, String valorTotalAtivoAtual,
                                                String valorTotalCompras, String valorTotalVendas,
                                                String quantoQueroTotal, String quantoFaltaTotal,
                                                Integer quantidadeQueTenho, Integer quantidadeQueFaltaTotal,
                                                String comprarOuAguardar, String lucroOuPerda);

    List<AcaoResponse> toAcoesResponse(List<Acao> acoes);
}
