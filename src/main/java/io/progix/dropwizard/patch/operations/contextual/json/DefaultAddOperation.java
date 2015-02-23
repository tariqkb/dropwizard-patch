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

package io.progix.dropwizard.patch.operations.contextual.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.progix.dropwizard.patch.JsonPatchValue;
import io.progix.dropwizard.patch.JsonPath;
import io.progix.dropwizard.patch.operations.contextual.ContextualAddOperation;
import io.progix.jackson.JsonPatchInstruction;
import io.progix.jackson.operations.AddOperation;

public class DefaultAddOperation<T> implements ContextualAddOperation<T> {

    private final ObjectMapper mapper;

    public DefaultAddOperation(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public T add(T context, JsonPath path, JsonPatchValue value) {
        JsonNode contextNode = mapper.convertValue(context, JsonNode.class);

        AddOperation
                .apply(new JsonPatchInstruction(JsonPatchInstruction.JsonPatchOperationType.ADD, path.getJsonPointer(),
                        value.getNode()), contextNode);

        JsonNode contextNode = mapper.convertValue(context, JsonNode.class);
    }
}
