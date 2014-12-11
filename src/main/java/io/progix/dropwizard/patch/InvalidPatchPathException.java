package io.progix.dropwizard.patch;

import io.progix.dropwizard.patch.explicit.JsonPath;

public class InvalidPatchPathException extends RuntimeException {
    public InvalidPatchPathException(JsonPath path) {
        super("The path '" + path + "' could not be matched and is not modifiable.");
    }
}
