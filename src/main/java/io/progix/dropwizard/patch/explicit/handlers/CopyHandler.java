package io.progix.dropwizard.patch.explicit.handlers;

import io.progix.dropwizard.patch.explicit.JsonPath;

/**
 * A handler to contain logic for the patch operation COPY used in the explicit mode of patching.
 * <p/>
 * This handler can be registered to a {@link io.progix.dropwizard.patch.explicit.PatchRequest} in a resource.
 * <p/>
 * For more information on what the COPY patch operation should do, reference RFC6902.
 */
public interface CopyHandler {

    /**
     * Method containing logic on how a COPY patch operation should accomplish for a given resource. A {@link io
     * .progix.dropwizard.patch.explicit.JsonPath} for the location of the value to copy and where that value should
     * be copied to are given.
     *
     * @param from The {@link io.progix.dropwizard.patch.explicit.JsonPath} for the location of the value to copy
     * @param path The {@link io.progix.dropwizard.patch.explicit.JsonPath} for the location the value should be
     *             copied to
     */
    public void copy(JsonPath from, JsonPath path);
}
