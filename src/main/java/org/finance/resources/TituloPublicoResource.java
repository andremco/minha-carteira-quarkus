package org.finance.resources;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.finance.models.request.acao.EditarAcaoRequest;
import org.finance.models.request.tituloPublico.EditarTituloPublicoRequest;
import org.finance.models.request.tituloPublico.SalvarTituloPublicoRequest;
import org.finance.models.response.Paginado;
import org.finance.models.response.ResponseApi;
import org.finance.models.response.acao.AcaoResponse;
import org.finance.models.response.tituloPublico.TituloPublicoResponse;
import org.finance.services.TituloPublicoService;

@Path("/tituloPublico")
@Consumes(MediaType.APPLICATION_JSON)
public class TituloPublicoResource {
    @Inject
    TituloPublicoService service;
    @ConfigProperty(name = "operacao.realizado.com.sucesso")
    String operacaoSucesso;
    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseApi<TituloPublicoResponse> salvar(@Valid SalvarTituloPublicoRequest request) {
        return new ResponseApi<>(service.salvar(request), new String[] {operacaoSucesso}, true);
    }
    @PUT
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseApi<TituloPublicoResponse> editar(@Valid EditarTituloPublicoRequest request) {
        return new ResponseApi<>(service.editar(request), new String[] {operacaoSucesso}, true);
    }
    @DELETE
    @Transactional
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseApi<TituloPublicoResponse> deletar(@PathParam("id") Integer id) {
        service.excluir(id);
        return new ResponseApi<>(new String[] {operacaoSucesso}, true);
    }
    @GET
    @Path("/filtrar")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseApi<Paginado<TituloPublicoResponse>> filtrar(@HeaderParam("pagina") int pagina, @HeaderParam("tamanho") int tamanho) {
        return new ResponseApi<>(service.filtrarAcoes(pagina, tamanho), new String[] {operacaoSucesso}, true);
    }
}