package io.progix.dropwizard.patch;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class InvalidPatchPathExceptionMapper implements ExceptionMapper<InvalidPatchPathException>{

    @Override
    public Response toResponse(InvalidPatchPathException exception) {
        return Response.status(422).entity(exception.getMessage()).build();
    }
}
