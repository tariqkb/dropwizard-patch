package io.progix.dropwizard.patch;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class PatchOperationNotSupportedExceptionMapper implements ExceptionMapper<PatchOperationNotSupportedException> {

    @Override
    public Response toResponse(PatchOperationNotSupportedException exception) {
        return Response.status(415).entity(exception.getMessage()).build();
    }
}
