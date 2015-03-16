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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import io.progix.dropwizard.patch.exception.PatchTestFailedException;
import io.progix.dropwizard.patch.operations.contextual.json.*;
import io.progix.jackson.JsonPatchOperation;

import java.util.List;

public class DefaultJsonPatch<T> extends ContextualJsonPatch<T> {

    @JsonIgnore
    private final ObjectMapper mapper;
    @JsonIgnore
    private final Class<? extends T> typeClass;

    @JsonIgnore
    private final JsonAddOperation jsonAddOperation;
    @JsonIgnore
    private final JsonRemoveOperation jsonRemoveOperation;
    @JsonIgnore
    private final JsonReplaceOperation jsonReplaceOperation;
    @JsonIgnore
    private final JsonMoveOperation jsonMoveOperation;
    @JsonIgnore
    private final JsonCopyOperation jsonCopyOperation;
    @JsonIgnore
    private final JsonTestOperation jsonTestOperation;

    /**
     * Constructs an instance using a list of {@link JsonPatchOperation}
     *
     * @param operations A list of {@link JsonPatchOperation}
     * @param typeClass Class is required to convert a JsonNode back into context class
     */
    public DefaultJsonPatch(List<JsonPatchOperation> operations, Class<? extends T> typeClass) {
        super(operations);

        this.typeClass = typeClass;
        this.mapper = Jackson.newObjectMapper();

        this.jsonAddOperation = new JsonAddOperation();
        this.jsonRemoveOperation = new JsonRemoveOperation();
        this.jsonReplaceOperation = new JsonReplaceOperation();
        this.jsonMoveOperation = new JsonMoveOperation();
        this.jsonCopyOperation = new JsonCopyOperation();
        this.jsonTestOperation = new JsonTestOperation();
    }

    @Override
    public T apply(T context) throws PatchTestFailedException {
        T copiedContext = PatchUtil.copy(context);

        for (JsonPatchOperation instruction : instructions) {
            JsonPath path = new JsonPath(instruction.getPath());

            switch (instruction.getOperation()) {
                case ADD:
                    if (addOperation == null) {
                        JsonNode node = mapper.convertValue(copiedContext, JsonNode.class);
                        node = jsonAddOperation.add(node, path, new JsonPatchValue(instruction.getValue()));
                        copiedContext = mapper.convertValue(node, typeClass);
                    } else {

                        addOperation.add(copiedContext, path, new JsonPatchValue(instruction.getValue()));
                    }
                    break;
                case COPY:
                    if (copyOperation == null) {
                        JsonNode node = mapper.convertValue(copiedContext, JsonNode.class);
                        node = jsonCopyOperation
                                .copy(node, new JsonPath(instruction.getFrom()), path);
                        copiedContext = mapper.convertValue(node, typeClass);
                    } else {

                        copyOperation
                                .copy(copiedContext, new JsonPath(instruction.getFrom()), path);
                    }
                    break;
                case MOVE:
                    if (moveOperation == null) {
                        JsonNode node = mapper.convertValue(copiedContext, JsonNode.class);
                        node = jsonMoveOperation
                                .move(node, new JsonPath(instruction.getFrom()), path);
                        copiedContext = mapper.convertValue(node, typeClass);
                    } else {

                        moveOperation
                                .move(copiedContext, new JsonPath(instruction.getFrom()), path);
                    }
                    break;
                case REMOVE:
                    if (removeOperation == null) {
                        JsonNode node = mapper.convertValue(copiedContext, JsonNode.class);
                        node = jsonRemoveOperation.remove(node, path);
                        copiedContext = mapper.convertValue(node, typeClass);
                    } else {

                        removeOperation.remove(copiedContext, path);
                    }
                    break;
                case REPLACE:
                    if (replaceOperation == null) {
                        JsonNode node = mapper.convertValue(copiedContext, JsonNode.class);
                        node = jsonReplaceOperation.replace(node, path, new JsonPatchValue(instruction.getValue()));
                        copiedContext = mapper.convertValue(node, typeClass);
                    } else {

                        replaceOperation.replace(copiedContext, path, new JsonPatchValue(instruction.getValue()));
                    }
                    break;
                case TEST:
                    if (testOperation == null) {
                        JsonNode node = mapper.convertValue(copiedContext, JsonNode.class);
                        jsonTestOperation.test(node, path, new JsonPatchValue(instruction.getValue()));
                    } else {

                        boolean success = testOperation
                                .test(copiedContext, path, new JsonPatchValue(instruction.getValue()));
                        if (!success) {
                            throw new PatchTestFailedException(path, instruction.getValue());
                        }
                    }
                    break;
                default:
                    break;
            }
        }

        return copiedContext;
    }
}
