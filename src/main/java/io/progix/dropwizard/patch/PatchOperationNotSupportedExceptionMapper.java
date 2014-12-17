/*
 * Copyright 2014 Tariq Bugrara
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.progix.dropwizard.patch;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * {@link ExceptionMapper} for {@link PatchOperationNotSupportedException}
 * <p/>
 * The patch response will return with a HTTP status code 415 Unsupported Media Type to signify that the specified patch
 * operation requested is not supported.
 */
public class PatchOperationNotSupportedExceptionMapper implements ExceptionMapper<PatchOperationNotSupportedException> {

    @Override
    public Response toResponse(PatchOperationNotSupportedException exception) {
        return Response.status(415).entity(exception.getMessage()).build();
    }
}
