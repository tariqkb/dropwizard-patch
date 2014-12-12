package io.progix.dropwizard.patch;

import io.progix.dropwizard.patch.explicit.JsonPath;

/**
 * This exception should be thrown in the explicit mode of patching when a path is not possible or has no
 * implementation.
 */
public class InvalidPatchPathException extends RuntimeException {

    /**
     * The {@link io.progix.dropwizard.patch.explicit.JsonPath} is used to give helpful information about what was
     * invalid
     *
     * @param path The {@link io.progix.dropwizard.patch.explicit.JsonPath} that was invalid during a patch operation
     */
    public InvalidPatchPathException(JsonPath path) {
        super("The path '" + path + "' could not be matched or is not modifiable.");
    }
}
