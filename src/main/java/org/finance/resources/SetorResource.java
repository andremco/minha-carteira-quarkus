package org.finance.resources;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.finance.models.request.setor.EditarSetorRequest;
import org.finance.models.request.setor.SalvarSetorRequest;
import org.finance.models.response.Paginado;
import org.finance.models.response.ResponseApi;
import org.finance.models.response.setor.SetorResponse;
import org.finance.services.SetorService;

import java.util.List;

@Path("/setor")
@Consumes(MediaType.APPLICATION_JSON)
public class SetorResource {
    @Inject
    SetorService setorService;
    @ConfigProperty(name = "operacao.realizado.com.sucesso")
    String operacaoSucesso;
    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseApi<SetorResponse> salvar(@Valid SalvarSetorRequest request) {
        return new ResponseApi<>(setorService.salvar(request), new String[] {operacaoSucesso}, true);
    }
    @PUT
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseApi<SetorResponse> editar(@Valid EditarSetorRequest request) {
        return new ResponseApi<>(setorService.editar(request), new String[] {operacaoSucesso}, true);
    }
    @DELETE
    @Transactional
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseApi<SetorResponse> deletar(@PathParam("id") Integer id) {
        setorService.excluir(id);
        return new ResponseApi<>(new String[] {operacaoSucesso}, true);
    }
    @GET
    @Path("/filtrar")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseApi<Paginado<SetorResponse>> filtrar(@HeaderParam("pagina") int pagina, @HeaderParam("tamanho") int tamanho) {
        return new ResponseApi<>(setorService.filtrarSetores(pagina, tamanho), new String[] {operacaoSucesso}, true);
    }
}
