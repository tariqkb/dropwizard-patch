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

package io.progix.dropwizard.patch.exception;

import io.progix.dropwizard.patch.JsonPath;

/**
 * This exception should be thrown in the explicit mode of patching when a path is not possible or has no
 * implementation.
 */
public class InvalidPatchPathException extends RuntimeException {

    /**
     * The {@link JsonPath} is used to give helpful information about what was invalid
     *
     * @param path The {@link JsonPath} that was invalid during a patch operation
     */
    public InvalidPatchPathException(JsonPath path) {
        super("The path '" + path + "' could not be matched or is not modifiable.");
    }
}
