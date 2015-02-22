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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.progix.dropwizard.patch.JsonPatchValue;
import io.progix.dropwizard.patch.JsonPath;
import io.progix.dropwizard.patch.exception.InvalidPatchPathException;
import io.progix.dropwizard.patch.operations.contextual.ContextualAddOperation;

import java.io.IOException;

public class DefaultAddOperation<T> implements ContextualAddOperation<T> {

    private final ObjectMapper mapper;

    public DefaultAddOperation(ObjectMapper mapper,) {
        this.mapper = mapper;
    }

    @Override
    public void add(T context, JsonPath path, JsonPatchValue value) {
        JsonNode root;
        try {
            JsonParser jp = mapper.getFactory().createParser(mapper.writeValueAsString(context));
            root = jp.getCodec().readTree(jp);
        } catch (IOException e) {
            throw new RuntimeException("An error occurred while serializing/deserializing given context. See stack trace for more information.", e);
        }

        int end = path.size() - 1;
        if (path.element(end).exists()) {

        }

        JsonNode targetNode = root.at(path.getJsonPointer());
        if (targetNode.isMissingNode()) {
            throw new InvalidPatchPathException(path);
        }

        if (targetNode.isArray()) {
            targetNode.
        } else if (targetNode.isObject()) {

        } else {
            targetNode.is
        }
    }
}
