package org.finance.resources;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.finance.models.data.Setor;
import org.finance.models.request.SetorRequest;
import org.finance.services.SetorService;

import java.time.LocalDateTime;
import java.util.List;

@Path("/setor")
public class SetorResource {

    @Inject
    SetorService setorService;

    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Setor salvar(SetorRequest request) {
        return setorService.salvar(request);
    }

    @PUT
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Setor editar(SetorRequest request) {
        return setorService.editar(request);
    }

    @GET
    @Path("/todos")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Setor> setores() {
        return setorService.all();
    }
}
