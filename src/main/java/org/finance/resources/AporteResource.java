package org.finance.resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import org.finance.services.AporteService;

@Path("/aporte")
@Consumes(MediaType.APPLICATION_JSON)
public class AporteResource {
    @Inject
    AporteService service;


}
