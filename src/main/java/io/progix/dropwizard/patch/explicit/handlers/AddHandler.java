/*
 * Copyright 2014 Tariq Bugrara
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.progix.dropwizard.patch.explicit.handlers;

import io.progix.dropwizard.patch.InvalidPatchPathException;
import io.progix.dropwizard.patch.explicit.JsonPatchValue;
import io.progix.dropwizard.patch.explicit.JsonPath;
import io.progix.dropwizard.patch.explicit.PatchRequest;

/**
 * A handler to contain logic for the patch operation ADD used in the explicit mode of patching.
 * <p/>
 * This handler can be registered to a {@link PatchRequest} in a resource.
 * <p/>
 * For more information on what the ADD patch operation should do, reference RFC6902.
 */
public interface AddHandler {

    /**
     * Method to contain logic on how a ADD operation should be preformed for a resource. An {@link
     * InvalidPatchPathException} should be thrown if the path given is not matched.
     *
     * @param path  The {@link JsonPath} for the location the value should be added.
     * @param value the {@link JsonPatchValue} for the value to be added.
     */
    public void add(JsonPath path, JsonPatchValue value);

}
