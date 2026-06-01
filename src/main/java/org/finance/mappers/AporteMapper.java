package org.finance.mappers;

import org.finance.models.data.mariadb.entities.Acao;
import org.finance.models.data.mariadb.entities.Aporte;
import org.finance.models.data.mariadb.entities.Moeda;
import org.finance.models.data.mariadb.entities.TituloPublico;
import org.finance.models.request.aporte.SalvarAporteRequest;
import org.finance.models.response.aporte.AporteResponse;
import org.finance.services.AporteService;
import org.finance.utils.Formatter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "jakarta-cdi", imports = {LocalDateTime.class, AporteService.class, Formatter.class}, uses = {AcaoMapper.class, TituloPublicoMapper.class, MoedaMapper.class})
public interface AporteMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataRegistroCriacao", expression = "java(LocalDateTime.now())")
    @Mapping(target = "dataRegistroEdicao", ignore = true)
    @Mapping(target = "dataRegistroRemocao", ignore = true)
    Aporte toAporte(SalvarAporteRequest request, Acao acao, TituloPublico tituloPublico, Moeda moeda);

    @Mapping(target = "id", expression = "java(AporteService.selecionarIdAtivo(acao, tituloPublico, moeda))")
    @Mapping(target = "movimentacao", expression = "java(AporteService.sinalizarCompraOuVenda(aporte.getMovimentacao()))")
    @Mapping(target = "preco", source = "aporte.preco")
    @Mapping(target = "dataRegistro", source = "aporte.dataRegistroCriacao")
    AporteResponse toAporteResponse(Aporte aporte, Acao acao, TituloPublico tituloPublico, Moeda moeda);

    @Mapping(target = "movimentacao", expression = "java(AporteService.sinalizarCompraOuVenda(aporte.getMovimentacao()))")
    @Mapping(target = "preco", source = "aporte.preco")
    @Mapping(target = "dataRegistro", source = "aporte.dataRegistroCriacao")
    AporteResponse toAporteResponse(Aporte aporte);

    List<AporteResponse> toAportesResponse(List<Aporte> aportes);
}
