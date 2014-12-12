package io.progix.dropwizard.patch;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * {@link javax.ws.rs.ext.ExceptionMapper} for {@link io.progix.dropwizard.patch.InvalidPatchPathException}
 * <p/>
 * The response will return with a HTTP status code 422 Unprocessable Entity to signify that the {@link io.progix
 * .dropwizard.patch.explicit.JsonPath} accessed is not valid.
 */
public class InvalidPatchPathExceptionMapper implements ExceptionMapper<InvalidPatchPathException> {

    @Override
    public Response toResponse(InvalidPatchPathException exception) {
        return Response.status(422).entity(exception.getMessage()).build();
    }
}
