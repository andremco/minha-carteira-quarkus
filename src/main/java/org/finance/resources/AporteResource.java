package org.finance.resources;

import com.arjuna.ats.jta.exceptions.NotImplementedException;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.inject.ConfigProperty;
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
    public ResponseApi<AcaoResponse> deletar(@PathParam("id") Integer id) {
        service.excluir(id);
        return new ResponseApi<>(new String[] {operacaoSucesso}, true);
    }
    @GET
    @Path("/filtrar")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseApi<Paginado<AporteResponse>> filtrar(@Min(value = 1, message = "{campo.tipo.ativo.informado.valor.range}")
                                                         @Max(value = 2, message = "{campo.tipo.ativo.informado.valor.range}")
                                                         @HeaderParam("tipoAtivo")
                                                         Integer tipoAtivo,
                                                         @HeaderParam("ativoId")
                                                         Integer ativoId,
                                                         @Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}$", message = "{campo.data.invalido}")
                                                         @HeaderParam("dataInicio")
                                                         String dataInicio,
                                                         @Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}$", message = "{campo.data.invalido}")
                                                         @HeaderParam("dataFim")
                                                         String dataFim,
                                                         @NotNull(message = "{campo.pagina.nao.informado}")
                                                         @HeaderParam("pagina")
                                                         Integer pagina,
                                                         @NotNull(message = "{campo.tamanho.nao.informado}")
                                                         @HeaderParam("tamanho")
                                                         Integer tamanho) {
        return new ResponseApi<>(service.filtrarAportes(tipoAtivo, ativoId, dataInicio, dataFim, pagina, tamanho), new String[] {operacaoSucesso}, true);
    }
}
