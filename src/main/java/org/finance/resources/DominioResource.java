package org.finance.resources;

import jakarta.inject.Inject;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.finance.models.response.ResponseApi;
import org.finance.models.response.dominio.DominioResponse;
import org.finance.services.DominioService;

import java.util.List;

@Path("/dominio")
@Consumes(MediaType.APPLICATION_JSON)
public class DominioResource {
    @Inject
    DominioService dominioService;
    @ConfigProperty(name = "operacao.realizado.com.sucesso")
    String operacaoSucesso;
    @GET
    @Path("/tipoAtivos")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseApi<List<DominioResponse>> tipoAtivos() {
        return new ResponseApi<>(dominioService.tipoAtivos(), new String[] {operacaoSucesso}, true);
    }
    @GET
    @Path("/setores/{tipoAtivo}")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseApi<List<DominioResponse>> setores(@Min(value = 1, message = "{campo.tipo.ativo.informado.valor.range}")
                                                      @Max(value = 4, message = "{campo.tipo.ativo.informado.valor.range}")
                                                      @PathParam("tipoAtivo") Integer tipoAtivo) {
        return new ResponseApi<>(dominioService.setores(tipoAtivo), new String[] {operacaoSucesso}, true);
    }
    @GET
    @Path("/setores")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseApi<List<DominioResponse>> setores() {
        return new ResponseApi<>(dominioService.setores(null), new String[] {operacaoSucesso}, true);
    }
}
