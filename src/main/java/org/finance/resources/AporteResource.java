package org.finance.resources;

import com.arjuna.ats.jta.exceptions.NotImplementedException;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.finance.models.request.acao.EditarAcaoRequest;
import org.finance.models.request.aporte.EditarAporteRequest;
import org.finance.models.request.aporte.SalvarAporteRequest;
import org.finance.models.response.Paginado;
import org.finance.models.response.ResponseApi;
import org.finance.models.response.acao.AcaoResponse;
import org.finance.models.response.aporte.AporteResponse;
import org.finance.services.AporteService;

@Path("/aporte")
@Consumes(MediaType.APPLICATION_JSON)
public class AporteResource {
    @Inject
    AporteService service;
    @ConfigProperty(name = "operacao.realizado.com.sucesso")
    String operacaoSucesso;
    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseApi<AporteResponse> salvar(@Valid SalvarAporteRequest request) throws NotImplementedException {
        return new ResponseApi<>(service.salvar(request), new String[] {operacaoSucesso}, true);
    }
    @PUT
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseApi<AporteResponse> editar(@Valid EditarAporteRequest request) {
        return new ResponseApi<>(service.editar(request), new String[] {operacaoSucesso}, true);
    }
    @DELETE
    @Transactional
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseApi<AporteResponse> deletar(@PathParam("id") Integer id) {
        service.excluir(id);
        return new ResponseApi<>(new String[] {operacaoSucesso}, true);
    }
    @GET
    @Path("/filtrar")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseApi<Paginado<AporteResponse>> filtrar(@HeaderParam("pagina") int pagina, @HeaderParam("tamanho") int tamanho) {
        return new ResponseApi<>(service.filtrarAportes(pagina, tamanho), new String[] {operacaoSucesso}, true);
    }
}
