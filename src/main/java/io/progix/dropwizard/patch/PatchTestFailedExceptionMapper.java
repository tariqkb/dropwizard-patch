package io.progix.dropwizard.patch;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * {@link javax.ws.rs.ext.ExceptionMapper} for {@link io.progix.dropwizard.patch.PatchTestFailedException}
 * <p/>
 * There is no information in RFC6902 describing the manner in which the call should fail. The response code 412
 * Precondition Failed was chosen as this was felt to best describe a TEST patch operation failure.
 */
public class PatchTestFailedExceptionMapper implements ExceptionMapper<PatchTestFailedException> {

    @Override
    public Response toResponse(PatchTestFailedException exception) {
        return Response.status(412).entity(exception.getMessage()).build();
    }

}
