package org.finance.resources;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.finance.models.request.acao.SalvarAcaoRequest;
import org.finance.models.response.ResponseApi;

@Path("/acao")
@Consumes(MediaType.APPLICATION_JSON)
public class AcaoResource {
    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseApi salvar(@Valid SalvarAcaoRequest request) {
        return new ResponseApi<>(null, new String[] {}, true);
    }
}
