package org.finance.resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.finance.models.response.Paginado;
import org.finance.models.response.ResponseApi;
import org.finance.models.response.acao.AcaoResponse;
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
    @Path("/categorias")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseApi<List<DominioResponse>> categorias() {
        return new ResponseApi<>(dominioService.categorias(), new String[] {operacaoSucesso}, true);
    }
    @GET
    @Path("/setores")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseApi<List<DominioResponse>> setores() {
        return new ResponseApi<>(dominioService.setores(), new String[] {operacaoSucesso}, true);
    }
}
