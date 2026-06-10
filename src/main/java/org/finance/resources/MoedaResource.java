package org.finance.resources;

import com.arjuna.ats.jta.exceptions.NotImplementedException;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.finance.models.request.moeda.EditarMoedaRequest;
import org.finance.models.request.moeda.SalvarMoedaRequest;
import org.finance.models.response.Paginado;
import org.finance.models.response.ResponseApi;
import org.finance.models.response.moeda.DetalharMoedaResponse;
import org.finance.models.response.moeda.MoedaResponse;
import org.finance.services.MoedaService;

@Path("/moeda")
@Consumes(MediaType.APPLICATION_JSON)
public class MoedaResource {
    @Inject
    MoedaService moedaService;
    @ConfigProperty(name = "operacao.realizado.com.sucesso")
    String operacaoSucesso;

    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseApi<MoedaResponse> salvar(@Valid SalvarMoedaRequest request) throws NotImplementedException {
        return new ResponseApi<>(moedaService.salvar(request), new String[] {operacaoSucesso}, true);
    }

    @PUT
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseApi<MoedaResponse> editar(@Valid EditarMoedaRequest request) {
        return new ResponseApi<>(moedaService.editar(request), new String[] {operacaoSucesso}, true);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseApi<DetalharMoedaResponse> obter(@PathParam("id") Integer id){
        return new ResponseApi<>(moedaService.detalharMoeda(id), new String[] {operacaoSucesso}, true);
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseApi<MoedaResponse> deletar(@PathParam("id") Integer id) {
        moedaService.excluir(id);
        return new ResponseApi<>(new String[] {operacaoSucesso}, true);
    }

    @GET
    @Path("/filtrar")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseApi<Paginado<MoedaResponse>> filtrar(@HeaderParam("descricaoMoeda") String descricaoMoeda,
                                                        @NotNull(message = "{campo.pagina.nao.informado}") @HeaderParam("pagina") Integer pagina,
                                                        @NotNull(message = "{campo.tamanho.nao.informado}") @HeaderParam("tamanho") Integer tamanho) {
        return new ResponseApi<>(moedaService.filtrarMoedas(descricaoMoeda, pagina, tamanho), new String[] {operacaoSucesso}, true);
    }
}
