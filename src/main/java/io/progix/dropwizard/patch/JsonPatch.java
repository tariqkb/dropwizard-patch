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
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.progix.dropwizard.patch.exception.PatchOperationNotSupportedException;
import io.progix.dropwizard.patch.operations.AddOperation;
import io.progix.dropwizard.patch.operations.CopyOperation;
import io.progix.dropwizard.patch.operations.MoveOperation;
import io.progix.dropwizard.patch.operations.RemoveOperation;
import io.progix.dropwizard.patch.operations.ReplaceOperation;
import io.progix.dropwizard.patch.operations.TestOperation;
import io.progix.jackson.JsonPatchOperation;
import io.progix.jackson.JsonPatchOperationType;
import io.progix.jackson.exceptions.JsonPatchTestFailedException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class is deserialized from an array of {@link JsonPatchOperation} and contains the explicit patch logic for use
 * in a resource method.
 * <p/>
 * Add this class as the entity for a resource method to support explicit patching. Deserialization is done
 * automatically using {@link JsonPatchDeserializer}.
 * <p/>
 * {@link JsonPatch#apply()} is used to apply all handler logic and should be called before the end of the resource
 * method for patching to occur.
 */
@JsonDeserialize(using = JsonPatchDeserializer.class)
public class JsonPatch {

    private List<JsonPatchOperation> operations;

    @JsonIgnore
    private AddOperation addOperation;
    @JsonIgnore
    private RemoveOperation removeOperation;
    @JsonIgnore
    private ReplaceOperation replaceOperation;
    @JsonIgnore
    private MoveOperation moveOperation;
    @JsonIgnore
    private CopyOperation copyOperation;
    @JsonIgnore
    private TestOperation testOperation;

    /**
     * Constructs an instance using a list of {@link JsonPatchOperation}
     *
     * @param operations A list of {@link JsonPatchOperation}
     */
    public JsonPatch(List<io.progix.jackson.JsonPatchOperation> operations) {
        this.operations = operations;
    }

    /**
     * @return The list of {@link JsonPatchOperation} this patch request is composed of.
     * <p/>
     * This may be useful for edge cases not covered with the operation handlers.
     */
    public List<JsonPatchOperation> getOperations() {
        return operations;
    }

    /**
     * Using available patch operation handlers, this method will iterate through all {@link JsonPatchOperation} in this
     * patch request and run the handler logic.
     * <p/>
     * Calling this method is required for the patch to be applied and is left to the user to decide where and when it
     * will applied in a resource method.
     *
     * @throws JsonPatchTestFailedException when a TEST patch operation fails
     * @see JsonPatchOperationType#TEST
     */
    public void apply() throws JsonPatchTestFailedException {
        Set<JsonPatchOperationType> unsupportedOperationTypes = new HashSet<>();

        for (JsonPatchOperation operation : operations) {
            JsonPath path = new JsonPath(operation.getPath());

            switch (operation.getOperation()) {
                case ADD:
                    if (addOperation == null) {
                        unsupportedOperationTypes.add(JsonPatchOperationType.ADD);
                    } else {

                        addOperation.add(path, new JsonPatchValue(operation.getValue()));
                    }
                    break;
                case COPY:
                    if (copyOperation == null) {
                        unsupportedOperationTypes.add(JsonPatchOperationType.COPY);
                    } else {

                        copyOperation.copy(new JsonPath(operation.getFrom()), new JsonPath(operation.getPath()));
                    }
                    break;
                case MOVE:
                    if (moveOperation == null) {
                        unsupportedOperationTypes.add(JsonPatchOperationType.MOVE);
                    } else {

                        moveOperation.move(new JsonPath(operation.getFrom()), new JsonPath(operation.getPath()));
                    }
                    break;
                case REMOVE:
                    if (removeOperation == null) {
                        unsupportedOperationTypes.add(JsonPatchOperationType.REMOVE);
                    } else {

                        removeOperation.remove(new JsonPath(operation.getPath()));
                    }
                    break;
                case REPLACE:
                    if (replaceOperation == null) {
                        unsupportedOperationTypes.add(JsonPatchOperationType.REPLACE);
                    } else {

                        replaceOperation.replace(new JsonPath(operation.getPath()), new JsonPatchValue(operation.getValue()));
                    }
                    break;
                case TEST:
                    if (testOperation == null) {
                        unsupportedOperationTypes.add(JsonPatchOperationType.TEST);
                    } else {

                        boolean success = testOperation.test(new JsonPath(operation.getPath()), new JsonPatchValue(operation.getValue()));
                        if (!success) {
                            throw new JsonPatchTestFailedException(operation.getPath(), operation.getValue(), "A test failed.");
                        }
                    }
                    break;
                default:
                    break;
            }
        }
        if (!unsupportedOperationTypes.isEmpty()) {
            throw new PatchOperationNotSupportedException(unsupportedOperationTypes);
        }
    }

    /**
     * Sets the {@link AddOperation} for the ADD patch operation.
     *
     * @param handler The {@link AddOperation} to use for this patch request
     * @see JsonPatchOperationType#ADD
     */
    public void setAdd(AddOperation handler) {
        this.addOperation = handler;
    }

    /**
     * Sets the {@link RemoveOperation} for the REMOVE patch operation.
     *
     * @param handler The {@link RemoveOperation} to use for this patch request
     * @see JsonPatchOperationType#REMOVE
     */
    public void setRemove(RemoveOperation handler) {
        this.removeOperation = handler;
    }

    /**
     * Sets the {@link ReplaceOperation} for the REMOVE patch operation.
     *
     * @param handler The {@link ReplaceOperation} to use for this patch request
     * @see JsonPatchOperationType#REPLACE
     */
    public void setReplace(ReplaceOperation handler) {
        this.replaceOperation = handler;
    }

    /**
     * Sets the {@link MoveOperation} for the REMOVE patch operation.
     *
     * @param handler The {@link MoveOperation} to use for this patch request
     * @see JsonPatchOperationType#MOVE
     */
    public void setMove(MoveOperation handler) {
        this.moveOperation = handler;
    }

    /**
     * Sets the {@link CopyOperation} for the REMOVE patch operation.
     *
     * @param handler The {@link CopyOperation} to use for this patch request
     * @see JsonPatchOperationType#COPY
     */
    public void setCopy(CopyOperation handler) {
        this.copyOperation = handler;
    }

    /**
     * Sets the {@link TestOperation} for the REMOVE patch operation.
     *
     * @param handler The {@link TestOperation} to use for this patch request
     * @see JsonPatchOperationType#TEST
     */
    public void setTest(TestOperation handler) {
        this.testOperation = handler;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        JsonPatch that = (JsonPatch) o;

        return operations.equals(that.operations);

    }

    @Override
    public int hashCode() {
        return operations.hashCode();
    }

    @Override
    public String toString() {
        return "JsonPatch{" +
                "operations=" + operations +
                '}';
    }
}
