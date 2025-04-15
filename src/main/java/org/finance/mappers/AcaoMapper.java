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
    @Mapping(target = "precoDinamico", ignore = true)
    @Mapping(target = "lucroOuPerda", ignore = true)
    @Mapping(target = "comprarOuAguardar", ignore = true)
    @Mapping(target = "valorTotalAtivoAtual", ignore = true)
    @Mapping(target = "urlIconAtivo", ignore = true)
    AcaoResponse toAcaoResponse(Acao acao, Setor setor);

    @Mapping(target = "dataRegistro", source = "acao.dataRegistroCriacao")
    @Mapping(target = "quantidade", ignore = true)
    @Mapping(target = "precoDinamico", ignore = true)
    @Mapping(target = "lucroOuPerda", ignore = true)
    @Mapping(target = "comprarOuAguardar", ignore = true)
    @Mapping(target = "valorTotalAtivoAtual", ignore = true)
    @Mapping(target = "urlIconAtivo", ignore = true)
    AcaoResponse toAcaoResponse(Acao acao);

    @Mapping(target = "dataRegistro", source = "acao.dataRegistroCriacao")
    @Mapping(target = "quantidade", source = "quantidadeQueTenho")
    AcaoResponse toAcaoResponse(Acao acao, double precoDinamico, Integer quantidadeQueTenho, double valorTotalAtivoAtual,
                                String comprarOuAguardar, double lucroOuPerda, String urlIconAtivo);

    @Mapping(target = "razaoSocial", source = "acao.razaoSocial")
    @Mapping(target = "ticker", source = "acao.ticker")
    @Mapping(target = "dataRegistro", source = "acao.dataRegistroCriacao")
    @Mapping(target = "quantidade", source = "quantidadeQueTenho")
    @Mapping(target = "urlIconAtivo", ignore = true)
    DetalharAcaoResponse toDetalharAcaoResponse(Acao acao, double precoDinamico,
                                                double carteiraIdealPorcento, double carteiraTenhoPorcento,
                                                double valorTotalAtivo, double valorTotalAtivoAtual,
                                                double valorTotalCompras, double valorTotalVendas,
                                                double quantoQueroTotal, double quantoFaltaTotal,
                                                Integer quantidadeQueTenho, Integer quantidadeQueFaltaTotal,
                                                String comprarOuAguardar, double lucroOuPerda);

    List<AcaoResponse> toAcoesResponse(List<Acao> acoes);
}
