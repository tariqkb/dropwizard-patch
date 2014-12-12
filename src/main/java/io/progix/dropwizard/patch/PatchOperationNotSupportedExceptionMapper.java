package io.progix.dropwizard.patch;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * {@link javax.ws.rs.ext.ExceptionMapper} for {@link io.progix.dropwizard.patch.PatchOperationNotSupportedException}
 * <p/>
 * The patch response will return with a HTTP status code 415 Unsupported Media Type to signify that the specified
 * patch operation requested is not supported.
 */
public class PatchOperationNotSupportedExceptionMapper implements ExceptionMapper<PatchOperationNotSupportedException> {

    @Override
    public Response toResponse(PatchOperationNotSupportedException exception) {
        return Response.status(415).entity(exception.getMessage()).build();
    }
}
