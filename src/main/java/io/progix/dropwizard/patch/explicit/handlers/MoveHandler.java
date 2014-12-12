package io.progix.dropwizard.patch.explicit.handlers;

import io.progix.dropwizard.patch.explicit.JsonPath;

/**
 * A handler to contain logic for the patch operation MOVE used in the explicit mode of patching.
 * <p/>
 * This handler can be registered to a {@link io.progix.dropwizard.patch.explicit.PatchRequest} in a resource.
 * <p/>
 * For more information on what the MOVE patch operation should do, reference RFC6902.
 */
public interface MoveHandler {

    /**
     * Method to contain logic on how a MOVE operation should be preformed for a resource. An {@link io.progix
     * .dropwizard.patch.InvalidPatchPathException} should be thrown if the path given is not matched.
     *
     * @param from the {@link io.progix.dropwizard.patch.explicit.JsonPath} for the location of the value to be moved.
     * @param path the {@link io.progix.dropwizard.patch.explicit.JsonPath} for the location the value should be
     *             moved to.
     */
    public void move(JsonPath from, JsonPath path);
}
