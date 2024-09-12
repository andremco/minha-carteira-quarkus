package org.finance.resources;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.finance.models.data.Setor;
import org.finance.models.request.SetorRequest;
import org.finance.services.SetorService;

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
}
