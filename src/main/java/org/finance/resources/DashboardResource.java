package org.finance.resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.finance.models.response.ResponseApi;
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
}
