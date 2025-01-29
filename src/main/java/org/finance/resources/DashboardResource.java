package org.finance.resources;

import com.arjuna.ats.jta.exceptions.NotImplementedException;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.finance.models.response.ResponseApi;
import org.finance.models.response.dashboard.AportesPorcetagemTotalResponse;
import org.finance.models.response.dashboard.AportesValorMensalResponse;
import org.finance.models.response.dashboard.AportesValorTotalResponse;
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
    public ResponseApi<ValoresCarteiraResponse> obterCarteiraTotal(){
        return new ResponseApi<>(service.obterCarteiraTotal(), new String[] {operacaoSucesso}, true);
    }

    @GET
    @Path("/aportes/porcentagem/total")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseApi<AportesPorcetagemTotalResponse> obterAportesPorcentagemTotal(){
        return new ResponseApi<>(service.obterAportesPorcentagemTotal(), new String[] {operacaoSucesso}, true);
    }

    @GET
    @Path("/aportes/valor/total")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseApi<AportesValorTotalResponse> obterAportesValorTotal(){
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
                                                                           String dataFim) throws NotImplementedException {
        return new ResponseApi<>(service.obterAportesValorMensal(dataInicio, dataFim), new String[] {operacaoSucesso}, true);
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
