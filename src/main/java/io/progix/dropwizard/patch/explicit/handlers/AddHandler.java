package io.progix.dropwizard.patch.explicit.handlers;

import io.progix.dropwizard.patch.explicit.JsonPatchValue;
import io.progix.dropwizard.patch.explicit.JsonPath;

/**
 * A handler to contain logic for the patch operation ADD used in the explicit mode of patching.
 * <p/>
 * This handler can be registered to a {@link io.progix.dropwizard.patch.explicit.PatchRequest} in a resource.
 * <p/>
 * For more information on what the ADD patch operation should do, reference RFC6902.
 */
public interface AddHandler {

    /**
     * Method to contain logic on how a ADD operation should be preformed for a resource. An {@link io.progix
     * .dropwizard.patch.InvalidPatchPathException} should be thrown if the path given is not matched.
     *
     * @param path  The {@link io.progix.dropwizard.patch.explicit.JsonPath} for the location the value should be added.
     * @param value the {@link io.progix.dropwizard.patch.explicit.JsonPatchValue} for the value to be added.
     */
    public void add(JsonPath path, JsonPatchValue value);

}
