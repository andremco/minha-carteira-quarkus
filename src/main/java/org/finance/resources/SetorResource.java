package org.finance.resources;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.finance.models.data.Setor;
import org.finance.models.request.setor.EditarSetorRequest;
import org.finance.models.request.setor.SalvarSetorRequest;
import org.finance.models.response.ResponseApi;
import org.finance.services.SetorService;

import java.util.List;

@Path("/setor")
public class SetorResource {

    @Inject
    SetorService setorService;

    public static final String OPERACAO_REALIZADO_COM_SUCESSO = "Operação realizado com sucesso!";

    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseApi<Setor> salvar(@Valid SalvarSetorRequest request) {
        return new ResponseApi<>(setorService.salvar(request), OPERACAO_REALIZADO_COM_SUCESSO, true);
    }

    @PUT
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseApi<Setor> editar(@Valid EditarSetorRequest request) {
        return new ResponseApi<>(setorService.editar(request), OPERACAO_REALIZADO_COM_SUCESSO, true);
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseApi<Setor> editar(@PathParam("id") Integer id) {
        setorService.excluir(id);
        return new ResponseApi<>(OPERACAO_REALIZADO_COM_SUCESSO, true);
    }

    @GET
    @Path("/todos")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Setor> setores() {
        return setorService.all();
    }
}
