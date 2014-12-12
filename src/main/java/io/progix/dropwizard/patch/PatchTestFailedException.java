package io.progix.dropwizard.patch;

import io.progix.dropwizard.patch.explicit.JsonPath;

/**
 * An exception thrown when a TEST patch operation fails.
 * <p/>
 * This exception is thrown automatically in the explicit patch method when the {@link io.progix.dropwizard.patch
 * .explicit.handlers.TestHandler} returns false
 */
public class PatchTestFailedException extends Exception {

    /**
     * Takes a {@link io.progix.dropwizard.patch.explicit.JsonPath} and a value to describe how the TEST operation
     * failed.
     *
     * @param path  The {@link io.progix.dropwizard.patch.explicit.JsonPath} the TEST operation failed with
     * @param value The value the TEST operation failed with
     */
    public PatchTestFailedException(JsonPath path, Object value) {
        super("A patch test operation failed. The value in '" + path
                .toString() + "' is not equivalent many the value '" + value.toString() + "'");
    }
}
