package io.progix.dropwizard.patch.explicit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.progix.dropwizard.patch.PatchOperationNotSupportedException;
import io.progix.dropwizard.patch.PatchTestFailedException;
import io.progix.dropwizard.patch.explicit.handlers.*;

import java.util.List;

/**
 * This class is deserialized from an array of {@link io.progix.dropwizard.patch.explicit.PatchInstruction} and contains
 * the explicit patch logic for use in a resource method.
 * <p/>
 * Add this class as the entity for a resource method to support explicit patching. Deserialization is done
 * automatically using {@link io.progix.dropwizard.patch.explicit.PatchRequestDeserializer}.
 * <p/>
 * {@link PatchRequest#apply()} is used to apply all handler logic and should be called before the end of the resource
 * method for patching to occur.
 */
@JsonDeserialize(using = PatchRequestDeserializer.class)
public class PatchRequest {

    private List<PatchInstruction> instructions;
    @JsonIgnore
    private AddHandler addHandler;
    @JsonIgnore
    private RemoveHandler removeHandler;
    @JsonIgnore
    private ReplaceHandler replaceHandler;
    @JsonIgnore
    private MoveHandler moveHandler;
    @JsonIgnore
    private CopyHandler copyHandler;
    @JsonIgnore
    private TestHandler testHandler;

    /**
     * Constructs an instance using a list of {@link io.progix.dropwizard.patch.explicit.PatchInstruction}
     *
     * @param instructions A list of {@link io.progix.dropwizard.patch.explicit.PatchInstruction}
     */
    public PatchRequest(List<PatchInstruction> instructions) {
        super();
        this.instructions = instructions;
    }

    /**
     * @return The list of {@link io.progix.dropwizard.patch.explicit.PatchInstruction} this patch request is composed
     * of.
     * <p/>
     * This may be useful for edge cases not covered with the operation handlers.
     */
    public List<PatchInstruction> getInstructions() {
        return instructions;
    }

    /**
     * Using available patch operation handlers, this method will iterate through all {@link
     * io.progix.dropwizard.patch.explicit.PatchInstruction} in this patch request and run the handler logic.
     * <p/>
     * Calling this method is required for the patch to be applied and is left to the user to decide where and when it
     * will applied in a resource method.
     *
     * @throws PatchTestFailedException when a TEST patch operation fails
     * @see io.progix.dropwizard.patch.explicit.PatchOperation#TEST
     */
    public void apply() throws PatchTestFailedException {
        for (PatchInstruction instruction : instructions) {
            JsonPath path = new JsonPath(JsonPointer.compile(instruction.getPath()));

            switch (instruction.getOperation()) {
                case ADD:
                    if (addHandler == null) {
                        throw new PatchOperationNotSupportedException(PatchOperation.ADD);
                    }

                    addHandler.add(path, new JsonPatchValue(instruction.getValue()));

                    break;
                case COPY:
                    if (copyHandler == null) {
                        throw new PatchOperationNotSupportedException(PatchOperation.COPY);
                    }

                    copyHandler.copy(new JsonPath(JsonPointer.compile(instruction.getFrom())), path);
                    break;
                case MOVE:
                    if (moveHandler == null) {
                        throw new PatchOperationNotSupportedException(PatchOperation.MOVE);
                    }

                    moveHandler.move(new JsonPath(JsonPointer.compile(instruction.getFrom())), path);
                    break;
                case REMOVE:
                    if (removeHandler == null) {
                        throw new PatchOperationNotSupportedException(PatchOperation.REMOVE);
                    }

                    removeHandler.remove(path);
                    break;
                case REPLACE:
                    if (replaceHandler == null) {
                        throw new PatchOperationNotSupportedException(PatchOperation.REPLACE);
                    }

                    replaceHandler.replace(path, new JsonPatchValue(instruction.getValue()));
                    break;
                case TEST:
                    if (testHandler == null) {
                        throw new PatchOperationNotSupportedException(PatchOperation.TEST);
                    }

                    boolean success = testHandler.test(path, new JsonPatchValue(instruction.getValue()));

                    if (!success) {
                        throw new PatchTestFailedException(path, instruction.getValue());
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Sets the {@link io.progix.dropwizard.patch.explicit.handlers.AddHandler} for the ADD patch operation.
     *
     * @param handler The {@link io.progix.dropwizard.patch.explicit.handlers.AddHandler} to use for this patch request
     * @see io.progix.dropwizard.patch.explicit.PatchOperation#ADD
     */
    public void add(AddHandler handler) {
        this.addHandler = handler;
    }

    /**
     * Sets the {@link io.progix.dropwizard.patch.explicit.handlers.RemoveHandler} for the REMOVE patch operation.
     *
     * @param handler The {@link io.progix.dropwizard.patch.explicit.handlers.RemoveHandler} to use for this patch
     *                request
     * @see io.progix.dropwizard.patch.explicit.PatchOperation#REMOVE
     */
    public void remove(RemoveHandler handler) {
        this.removeHandler = handler;
    }

    /**
     * Sets the {@link io.progix.dropwizard.patch.explicit.handlers.ReplaceHandler} for the REMOVE patch operation.
     *
     * @param handler The {@link io.progix.dropwizard.patch.explicit.handlers.ReplaceHandler} to use for this patch
     *                request
     * @see io.progix.dropwizard.patch.explicit.PatchOperation#REPLACE
     */
    public void replace(ReplaceHandler handler) {
        this.replaceHandler = handler;
    }

    /**
     * Sets the {@link io.progix.dropwizard.patch.explicit.handlers.MoveHandler} for the REMOVE patch operation.
     *
     * @param handler The {@link io.progix.dropwizard.patch.explicit.handlers.MoveHandler} to use for this patch
     *                request
     * @see io.progix.dropwizard.patch.explicit.PatchOperation#MOVE
     */
    public void move(MoveHandler handler) {
        this.moveHandler = handler;
    }

    /**
     * Sets the {@link io.progix.dropwizard.patch.explicit.handlers.CopyHandler} for the REMOVE patch operation.
     *
     * @param handler The {@link io.progix.dropwizard.patch.explicit.handlers.CopyHandler} to use for this patch
     *                request
     * @see io.progix.dropwizard.patch.explicit.PatchOperation#COPY
     */
    public void copy(CopyHandler handler) {
        this.copyHandler = handler;
    }

    /**
     * Sets the {@link io.progix.dropwizard.patch.explicit.handlers.TestHandler} for the REMOVE patch operation.
     *
     * @param handler The {@link io.progix.dropwizard.patch.explicit.handlers.TestHandler} to use for this patch
     *                request
     * @see io.progix.dropwizard.patch.explicit.PatchOperation#TEST
     */
    public void test(TestHandler handler) {
        this.testHandler = handler;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        PatchRequest that = (PatchRequest) o;

        if (!instructions.equals(that.instructions))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return instructions.hashCode();
    }
}
