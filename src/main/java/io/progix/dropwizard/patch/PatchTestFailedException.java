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

package io.progix.dropwizard.patch;

import io.progix.dropwizard.patch.explicit.JsonPath;
import io.progix.dropwizard.patch.explicit.handlers.TestHandler;

/**
 * An exception thrown when a TEST patch operation fails.
 * <p/>
 * This exception is thrown automatically in the explicit patch method when the {@link TestHandler} returns false
 */
public class PatchTestFailedException extends Exception {

    /**
     * Takes a {@link JsonPath} and a value to describe how the TEST operation failed.
     *
     * @param path  The {@link JsonPath} the TEST operation failed with
     * @param value The value the TEST operation failed with
     */
    public PatchTestFailedException(JsonPath path, Object value) {
        super("A patch test operation failed. The value in '" + path
                .toString() + "' is not equivalent to the value '" + value.toString() + "'");
    }
}
