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

package io.progix.dropwizard.patch.operations;

import io.progix.dropwizard.patch.BasicJsonPatch;
import io.progix.dropwizard.patch.JsonPath;

/**
 * A handler to contain logic for the patch operation COPY used in the explicit mode of patching.
 * <p/>
 * This handler can be registered to a {@link BasicJsonPatch} in a resource.
 * <p/>
 * For more information on what the COPY patch operation should do, reference RFC6902.
 */
public interface CopyOperation {

    /**
     * Method containing logic on how a COPY patch operation should accomplish for a given resource. A {@link JsonPath}
     * for the location of the value to copy and where that value should be copied to are given.
     *
     * @param from    The {@link JsonPath} for the location of the value to copy
     * @param path    The {@link JsonPath} for the location the value should be copied to
     */
    public void copy(JsonPath from, JsonPath path);
}
