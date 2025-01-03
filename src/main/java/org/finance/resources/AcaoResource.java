package org.finance.resources;

import com.arjuna.ats.jta.exceptions.NotImplementedException;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.finance.models.request.acao.EditarAcaoRequest;
import org.finance.models.request.acao.SalvarAcaoRequest;
import org.finance.models.response.Paginado;
import org.finance.models.response.ResponseApi;
import org.finance.models.response.acao.AcaoResponse;
import org.finance.models.response.acao.DetalharAcaoResponse;
import org.finance.services.AcaoService;

@Path("/acao")
@Consumes(MediaType.APPLICATION_JSON)
public class AcaoResource {
    @Inject
    AcaoService acaoService;
    @ConfigProperty(name = "operacao.realizado.com.sucesso")
    String operacaoSucesso;
    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseApi<AcaoResponse> salvar(@Valid SalvarAcaoRequest request) throws NotImplementedException {
        return new ResponseApi<>(acaoService.salvar(request), new String[] {operacaoSucesso}, true);
    }
    @PUT
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseApi<AcaoResponse> editar(@Valid EditarAcaoRequest request) {
        return new ResponseApi<>(acaoService.editar(request), new String[] {operacaoSucesso}, true);
    }
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseApi<DetalharAcaoResponse> obter(@PathParam("id") Integer id){
        return new ResponseApi<>(acaoService.detalharAcao(id), new String[] {operacaoSucesso}, true);
    }
    @DELETE
    @Transactional
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseApi<AcaoResponse> deletar(@PathParam("id") Integer id) {
        acaoService.excluir(id);
        return new ResponseApi<>(new String[] {operacaoSucesso}, true);
    }
    @GET
    @Path("/filtrar")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseApi<Paginado<AcaoResponse>> filtrar(@Min(value = 1, message = "{campo.tipo.ativo.informado.valor.range}")
                                                       @Max(value = 4, message = "{campo.tipo.ativo.informado.valor.range}")
                                                       @HeaderParam("tipoAtivoId") Integer tipoAtivoId,
                                                       @HeaderParam("razaoSocial") String razaoSocial,
                                                       @NotNull(message = "{campo.pagina.nao.informado}") @HeaderParam("pagina") Integer pagina,
                                                       @NotNull(message = "{campo.tamanho.nao.informado}") @HeaderParam("tamanho") Integer tamanho) {
        return new ResponseApi<>(acaoService.filtrarAcoes(tipoAtivoId, razaoSocial, pagina, tamanho), new String[] {operacaoSucesso}, true);
    }
}
