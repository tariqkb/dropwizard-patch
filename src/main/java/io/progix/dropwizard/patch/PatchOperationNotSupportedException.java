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

/**
 *
 */
package io.progix.dropwizard.patch;

import io.progix.dropwizard.patch.explicit.PatchOperation;

/**
 * This exception is thrown automatically when the explicit mode of patching is used and is missing a handler for the
 * specified patch operation.
 */
public class PatchOperationNotSupportedException extends RuntimeException {

    /**
     * @param operation The {@link PatchOperation} not supported for this resource.
     */
    public PatchOperationNotSupportedException(PatchOperation operation) {
        super("The PATCH operation " + operation.name() + " is not supported for this resource.");
    }

}
