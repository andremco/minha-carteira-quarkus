package org.finance.resources;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.finance.models.request.tituloPublico.SalvarTituloPublicoRequest;
import org.finance.models.response.ResponseApi;
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
}
