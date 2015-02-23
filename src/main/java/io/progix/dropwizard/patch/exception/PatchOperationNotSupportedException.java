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
package io.progix.dropwizard.patch.exception;

import io.progix.dropwizard.patch.JsonPatchOperationType;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;

/**
 * This exception is thrown automatically when a handler is missing for a patch request
 */
public class PatchOperationNotSupportedException extends RuntimeException {

    private final Set<JsonPatchOperationType> operations;

    /**
     * @param operations A set of  {@link io.progix.dropwizard.patch.JsonPatchOperationType} not supported for this
     *                   resource.
     */
    public PatchOperationNotSupportedException(Set<JsonPatchOperationType> operations) {
        super("The PATCH operations " + StringUtils.join(operations, ',') + " is not supported for this resource.");
        this.operations = operations;
    }

    public Set<JsonPatchOperationType> getOperations() {
        return operations;
    }
}
