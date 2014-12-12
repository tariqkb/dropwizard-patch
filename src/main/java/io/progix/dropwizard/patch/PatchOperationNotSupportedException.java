/**
 *
 */
package io.progix.dropwizard.patch;

import io.progix.dropwizard.patch.explicit.PatchOperation;

/**
 * This exception is thrown automatically when the explicit mode of patching is used and is missing a handler for the
 * specified patch operation.
 */
public class PatchOperationNotSupportedException extends RuntimeException {

    /**
     * @param operation The {@link io.progix.dropwizard.patch.explicit.PatchOperation} not supported for this resource.
     */
    public PatchOperationNotSupportedException(PatchOperation operation) {
        super("The PATCH operation " + operation.name() + " is not supported for this resource.");
    }

}
