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
import io.progix.dropwizard.patch.JsonPatchValue;
import io.progix.dropwizard.patch.JsonPath;
import io.progix.dropwizard.patch.exception.InvalidPatchPathException;

/**
 * A handler to contain logic for the patch operation TEST used in the explicit mode of patching.
 * <p/>
 * This handler can be registered to a {@link BasicJsonPatch} in a resource.
 * <p/>
 * For more information on what the TEST patch operation should do, reference RFC6902.
 */
public interface TestOperation {

    /**
     * Method to contain logic on how a TEST operation should be preformed for a resource. An {@link
     * InvalidPatchPathException} should be thrown if the path given is not matched.
     *
     * @param path  The {@link JsonPath} for the location of the value to test.
     * @param value The {@link JsonPatchValue} to check for equality
     * @return true if the operation passes, false otherwise.
     */
    public boolean test(JsonPath path, JsonPatchValue value);

}