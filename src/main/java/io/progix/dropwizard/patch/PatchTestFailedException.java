package io.progix.dropwizard.patch;

import io.progix.dropwizard.patch.explicit.JsonPath;

public class PatchTestFailedException extends Exception {

    public PatchTestFailedException(JsonPath path, Object value) {
        super("A patch test operation failed. The value in '" + path.toString() + "' is not equivalent to the value '" + value.toString() + "'");
    }
}
