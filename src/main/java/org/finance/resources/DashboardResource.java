package org.finance.resources;

import com.arjuna.ats.jta.exceptions.NotImplementedException;
import jakarta.inject.Inject;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.finance.models.response.ResponseApi;
import org.finance.models.response.dashboard.AportesTotalResponse;
import org.finance.models.response.dashboard.AportesValorMensalResponse;
import org.finance.models.response.dashboard.SetoresFatiadoResponse;
import org.finance.models.response.dashboard.ValoresCarteiraResponse;
import org.finance.services.DashboardService;

import java.util.List;

@Path("/dashboard")
@Consumes(MediaType.APPLICATION_JSON)
public class DashboardResource {
    @ConfigProperty(name = "operacao.realizado.com.sucesso")
    String operacaoSucesso;

    @Inject
    DashboardService service;

    @GET
    @Path("/carteira/total")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseApi<ValoresCarteiraResponse> obterCarteiraTotal(){
        return new ResponseApi<>(service.obterCarteiraTotal(), new String[] {operacaoSucesso}, true);
    }

    @GET
    @Path("/aportes/porcentagem/total")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseApi<AportesTotalResponse> obterAportesPorcentagemTotal(){
        return new ResponseApi<>(service.obterAportesPorcentagemTotal(), new String[] {operacaoSucesso}, true);
    }

    @GET
    @Path("/aportes/valor/total")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseApi<AportesTotalResponse> obterAportesValorTotal(){
        return new ResponseApi<>(service.obterAportesValorTotal(), new String[] {operacaoSucesso}, true);
    }

    @GET
    @Path("/aportes/valor/mensal")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseApi<AportesValorMensalResponse> obterAportesValorMensal(@Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}$", message = "{campo.data.invalido}")
                                                                           @HeaderParam("dataInicio")
                                                                           @NotNull(message = "{campo.data.inicio.nao.informado}")
                                                                           String dataInicio,
                                                                           @Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}$", message = "{campo.data.invalido}")
                                                                           @NotNull(message = "{campo.data.fim.nao.informado}")
                                                                           @HeaderParam("dataFim")
                                                                           String dataFim)  {
        return new ResponseApi<>(service.obterAportesValorMensal(dataInicio, dataFim), new String[] {operacaoSucesso}, true);
    }

    @GET
    @Path("/setores/fatiado/{tipoAtivoId}")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseApi<List<SetoresFatiadoResponse>> obterSetoresFatiado(@Min(value = 1, message = "{campo.tipo.ativo.informado.valor.range}")
                                                                         @Max(value = 4, message = "{campo.tipo.ativo.informado.valor.range}")
                                                                         @PathParam("tipoAtivoId") Integer tipoAtivoId){
        return new ResponseApi<>(service.obterSetoresFatiado(tipoAtivoId), new String[] {operacaoSucesso}, true);
    }

    @GET
    @Path("setores/aumento/fatiado/{tipoAtivoId}")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseApi<List<SetoresFatiadoResponse>> obterSetoresAumentoFatiado(@Min(value = 1, message = "{campo.tipo.ativo.informado.valor.range}")
                                                                                @Max(value = 4, message = "{campo.tipo.ativo.informado.valor.range}")
                                                                                @PathParam("tipoAtivoId") Integer tipoAtivoId){
        return new ResponseApi<>(service.obterSetoresAumentoFatiado(tipoAtivoId), new String[] {operacaoSucesso}, true);
    }
}
