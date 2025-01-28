package org.finance.resources;

import com.arjuna.ats.jta.exceptions.NotImplementedException;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.finance.models.response.ResponseApi;
import org.finance.models.response.dashboard.AportesMensalResponse;
import org.finance.models.response.dashboard.AportesTotalResponse;
import org.finance.models.response.dashboard.ValoresCarteiraResponse;
import org.finance.services.DashboardService;

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
    public ResponseApi<ValoresCarteiraResponse> obterValoresCarteira(){
        return new ResponseApi<>(service.obterValoresCarteira(), new String[] {operacaoSucesso}, true);
    }

    @GET
    @Path("/aportes/mensal")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseApi<AportesMensalResponse> obterAportesMensal(@Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}$", message = "{campo.data.invalido}")
                                                                 @HeaderParam("dataInicio")
                                                                 @NotNull(message = "{campo.data.inicio.nao.informado}")
                                                                 String dataInicio,
                                                                 @Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}$", message = "{campo.data.invalido}")
                                                                 @NotNull(message = "{campo.data.fim.nao.informado}")
                                                                 @HeaderParam("dataFim")
                                                                 String dataFim) throws NotImplementedException {
        return new ResponseApi<>(service.obterAportesMensal(dataInicio, dataFim), new String[] {operacaoSucesso}, true);
    }

    @GET
    @Path("/aportes/total")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseApi<AportesTotalResponse> obterAportesTotal(){
        return new ResponseApi<>(service.obterAportesTotal(), new String[] {operacaoSucesso}, true);
    }

    @GET
    @Path("/setor/tituloPublico")
    @Produces(MediaType.APPLICATION_JSON)
    public void obterSetorTituloPublico(){

    }

    @GET
    @Path("aumentar/setor/tituloPublico")
    @Produces(MediaType.APPLICATION_JSON)
    public void obterAumentoSetorTituloPublico(){

    }

    @GET
    @Path("/setores/acao")
    @Produces(MediaType.APPLICATION_JSON)
    public void obterSetoresAcao(){

    }

    @GET
    @Path("aumentar/setores/acao")
    @Produces(MediaType.APPLICATION_JSON)
    public void obterAumentoSetoresAcao(){

    }
}
