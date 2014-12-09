package io.progix.dropwizard.patch.hooks;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.progix.dropwizard.patch.PatchOperationNotSupportedException;
import io.progix.dropwizard.patch.hooks.handlers.*;

import java.util.List;

/**
 * TODO: Need to implement deserializer to convert an array into this object
 *
 * @author tariq
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

    public PatchRequest(List<PatchInstruction> instructions) {
        super();
        this.instructions = instructions;
    }

    public List<PatchInstruction> getInstructions() {
        return instructions;
    }

    public void apply() {
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

                    moveHandler.move(new JsonPath(JsonPointer.compile(instruction.getFrom())),
                            path);
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

                    testHandler.test(path, new JsonPatchValue(instruction.getValue()));
                    break;
                default:
                    break;
            }
        }
    }

    public void add(AddHandler handler) {
        this.addHandler = handler;
    }

    public void remove(RemoveHandler handler) {
        this.removeHandler = handler;
    }

    public void replace(ReplaceHandler handler) {
        this.replaceHandler = handler;
    }

    public void move(MoveHandler handler) {
        this.moveHandler = handler;
    }

    public void copy(CopyHandler handler) {
        this.copyHandler = handler;
    }

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
