package io.progix.dropwizard.patch;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class PatchTestFailedExceptionMapper implements ExceptionMapper<PatchTestFailedException> {

    @Override
    public Response toResponse(PatchTestFailedException exception) {
        return Response.status(400).entity(exception.getMessage()).build();
    }

}
