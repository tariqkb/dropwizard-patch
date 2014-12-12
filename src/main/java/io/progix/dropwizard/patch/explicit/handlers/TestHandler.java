package io.progix.dropwizard.patch.explicit.handlers;


import io.progix.dropwizard.patch.explicit.JsonPatchValue;
import io.progix.dropwizard.patch.explicit.JsonPath;

/**
 * A handler to contain logic for the patch operation TEST used in the explicit mode of patching.
 * <p/>
 * This handler can be registered to a {@link io.progix.dropwizard.patch.explicit.PatchRequest} in a resource.
 * <p/>
 * For more information on what the TEST patch operation should do, reference RFC6902.
 */
public interface TestHandler {

    /**
     * Method to contain logic on how a TEST operation should be preformed for a resource. An {@link io.progix
     * .dropwizard.patch.InvalidPatchPathException} should be thrown if the path given is not matched.
     *
     * @param path  The {@link io.progix.dropwizard.patch.explicit.JsonPath} for the location of the value to test.
     * @param value The {@link io.progix.dropwizard.patch.explicit.JsonPatchValue} to check for equality
     * @return true if the test passes, false otherwise.
     */
    public boolean test(JsonPath path, JsonPatchValue value);

}