package io.progix.dropwizard.patch.explicit.handlers;

import io.progix.dropwizard.patch.explicit.JsonPatchValue;
import io.progix.dropwizard.patch.explicit.JsonPath;

/**
 * A handler to contain logic for the patch operation REPLACE used in the explicit mode of patching.
 * <p/>
 * This handler can be registered to a {@link io.progix.dropwizard.patch.explicit.PatchRequest} in a resource.
 * <p/>
 * For more information on what the REPLACE patch operation should do, reference RFC6902.
 */
public interface ReplaceHandler {

    /**
     * Method to contain logic on how a REPLACE operation should be preformed for a resource. An {@link io.progix
     * .dropwizard.patch.InvalidPatchPathException} should be thrown if the path given is not matched.
     *
     * @param path  The {@link io.progix.dropwizard.patch.explicit.JsonPath} for the location of what value to be
     *              replaced.
     * @param value The {@link io.progix.dropwizard.patch.explicit.JsonPatchValue} that replaces the location
     *              specified by the path parameter
     */
    public void replace(JsonPath path, JsonPatchValue value);

}
