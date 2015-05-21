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
import io.progix.dropwizard.patch.operations.contextual.ContextualAddOperation;
import io.progix.dropwizard.patch.operations.contextual.ContextualCopyOperation;
import io.progix.dropwizard.patch.operations.contextual.ContextualMoveOperation;
import io.progix.dropwizard.patch.operations.contextual.ContextualRemoveOperation;
import io.progix.dropwizard.patch.operations.contextual.ContextualReplaceOperation;
import io.progix.dropwizard.patch.operations.contextual.ContextualTestOperation;
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
 * automatically using {@link BasicJsonPatchDeserializer}.
 * <p/>
 * {@link ContextualJsonPatch#apply(T)} is used to apply all handler logic and should be called before the end of the
 * resource method for patching to occur.
 * <p/>
 * This class is useful for patching a copy of given context instead of directly modifying it
 *
 * @param <T> The type of object that will be patched
 */
@JsonDeserialize(using = ContextualJsonPatchDeserializer.class)
public class ContextualJsonPatch<T> {

    protected List<JsonPatchOperation> operations;

    @JsonIgnore
    protected ContextualAddOperation<T> addOperation;
    @JsonIgnore
    protected ContextualRemoveOperation<T> removeOperation;
    @JsonIgnore
    protected ContextualReplaceOperation<T> replaceOperation;
    @JsonIgnore
    protected ContextualMoveOperation<T> moveOperation;
    @JsonIgnore
    protected ContextualCopyOperation<T> copyOperation;
    @JsonIgnore
    protected ContextualTestOperation<T> testOperation;

    /**
     * Constructs an instance using a list of {@link JsonPatchOperation}
     *
     * @param operations A list of {@link JsonPatchOperation}
     */
    public ContextualJsonPatch(List<JsonPatchOperation> operations) {
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
     * <p/>
     * Note that the context given will be copied using Jackson and will NOT be modified.
     *
     * @return a copy of the given context with all patch operation preformed.
     * @throws JsonPatchTestFailedException when a TEST patch operation fails
     * @see JsonPatchOperationType#TEST
     */
    public T apply(T context) throws JsonPatchTestFailedException {

        T copiedContext = PatchUtil.copy(context);

        Set<JsonPatchOperationType> unsupportedOperationTypes = new HashSet<>();

        for (JsonPatchOperation operation : operations) {
            JsonPath path = new JsonPath(operation.getPath());

            switch (operation.getOperation()) {
                case ADD:
                    if (addOperation == null) {
                        unsupportedOperationTypes.add(JsonPatchOperationType.ADD);
                    } else {

                        copiedContext = addOperation.add(copiedContext, path, new JsonPatchValue(operation.getValue()));
                    }
                    break;
                case COPY:
                    if (copyOperation == null) {
                        unsupportedOperationTypes.add(JsonPatchOperationType.COPY);
                    } else {

                        copiedContext = copyOperation.copy(copiedContext, new JsonPath(operation.getFrom()), path);
                    }
                    break;
                case MOVE:
                    if (moveOperation == null) {
                        unsupportedOperationTypes.add(JsonPatchOperationType.MOVE);
                    } else {

                        copiedContext = moveOperation.move(copiedContext, new JsonPath(operation.getFrom()), path);
                    }
                    break;
                case REMOVE:
                    if (removeOperation == null) {
                        unsupportedOperationTypes.add(JsonPatchOperationType.REMOVE);
                    } else {

                        copiedContext = removeOperation.remove(copiedContext, path);
                    }
                    break;
                case REPLACE:
                    if (replaceOperation == null) {
                        unsupportedOperationTypes.add(JsonPatchOperationType.REPLACE);
                    } else {

                        copiedContext = replaceOperation.replace(copiedContext, path, new JsonPatchValue(operation.getValue()));
                    }
                    break;
                case TEST:
                    if (testOperation == null) {
                        unsupportedOperationTypes.add(JsonPatchOperationType.TEST);
                    } else {

                        boolean success = testOperation.test(copiedContext, path, new JsonPatchValue(operation.getValue()));
                        if (!success) {
                            throw new JsonPatchTestFailedException(operation.getPath(), operation.getValue(), "A test failed");
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

        return copiedContext;
    }

    /**
     * Sets the {@link ContextualAddOperation} for the ADD patch operation.
     *
     * @param handler The {@link ContextualAddOperation} to use for this patch request
     * @see JsonPatchOperationType#ADD
     */
    public void setAdd(ContextualAddOperation<T> handler) {
        this.addOperation = handler;
    }

    /**
     * Sets the {@link ContextualRemoveOperation} for the REMOVE patch operation.
     *
     * @param handler The {@link ContextualRemoveOperation} to use for this patch request
     * @see JsonPatchOperationType#REMOVE
     */
    public void setRemove(ContextualRemoveOperation<T> handler) {
        this.removeOperation = handler;
    }

    /**
     * Sets the {@link ContextualReplaceOperation} for the REMOVE patch operation.
     *
     * @param handler The {@link ContextualReplaceOperation} to use for this patch request
     * @see JsonPatchOperationType#REPLACE
     */
    public void setReplace(ContextualReplaceOperation<T> handler) {
        this.replaceOperation = handler;
    }

    /**
     * Sets the {@link ContextualMoveOperation} for the REMOVE patch operation.
     *
     * @param handler The {@link ContextualMoveOperation} to use for this patch request
     * @see JsonPatchOperationType#MOVE
     */
    public void setMove(ContextualMoveOperation<T> handler) {
        this.moveOperation = handler;
    }

    /**
     * Sets the {@link ContextualCopyOperation} for the REMOVE patch operation.
     *
     * @param handler The {@link ContextualCopyOperation} to use for this patch request
     * @see JsonPatchOperationType#COPY
     */
    public void setCopy(ContextualCopyOperation<T> handler) {
        this.copyOperation = handler;
    }

    /**
     * Sets the {@link ContextualTestOperation} for the REMOVE patch operation.
     *
     * @param handler The {@link ContextualTestOperation} to use for this patch request
     * @see JsonPatchOperationType#TEST
     */
    public void setTest(ContextualTestOperation<T> handler) {
        this.testOperation = handler;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        ContextualJsonPatch that = (ContextualJsonPatch) o;

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
