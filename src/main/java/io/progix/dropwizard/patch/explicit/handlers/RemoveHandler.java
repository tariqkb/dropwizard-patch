package io.progix.dropwizard.patch.explicit.handlers;


import io.progix.dropwizard.patch.explicit.JsonPath;

/**
 * A handler to contain logic for the patch operation REMOVE used in the explicit mode of patching.
 * <p/>
 * This handler can be registered to a {@link io.progix.dropwizard.patch.explicit.PatchRequest} in a resource.
 * <p/>
 * For more information on what the REMOVE patch operation should do, reference RFC6902.
 */
public interface RemoveHandler {

    /**
     * Method to contain logic on how a REMOVE operation should be preformed for a resource. An {@link io.progix
     * .dropwizard.patch.InvalidPatchPathException} should be thrown if the path given is not matched.
     *
     * @param path The {@link io.progix.dropwizard.patch.explicit.JsonPath} for the location of the value to be removed.
     */
    public void remove(JsonPath path);

}
