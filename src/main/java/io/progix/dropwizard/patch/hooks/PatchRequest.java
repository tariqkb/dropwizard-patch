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

    public PatchRequest(List<PatchInstruction> instructions) {
        super();
        this.instructions = instructions;
    }

    public List<PatchInstruction> getInstructions() {
        return instructions;
    }

    public void process() {
        for (PatchInstruction instruction : instructions) {
            switch (instruction.getOperation()) {
                case ADD:
                    if (addHandler == null) {
                        throw new PatchOperationNotSupportedException(PatchOperation.ADD);
                    }

				    /* Get last path element to determine if this add operation adds to an array */
                    JsonPointer path = JsonPointer.compile(instruction.getPath());
                    while (path.tail() != null) {
                        path = path.tail();
                    }

                    String lastPointer = path.getMatchingProperty();
                    try {
                        Integer index = Integer.parseInt(lastPointer);
                        addHandler
                                .addElement(JsonPointer.compile(instruction.getPath()), index, instruction.getValue());
                    } catch (NumberFormatException e) {
                        addHandler.add(path, instruction.getValue());
                    }

                    break;
                case COPY:
                    if (copyHandler == null) {
                        throw new PatchOperationNotSupportedException(PatchOperation.ADD);
                    }

                    copyHandler.copy(JsonPointer.compile(instruction.getFrom()),
                            JsonPointer.compile(instruction.getPath()));
                    break;
                case MOVE:
                    if (moveHandler == null) {
                        throw new PatchOperationNotSupportedException(PatchOperation.ADD);
                    }

                    moveHandler.move(JsonPointer.compile(instruction.getFrom()),
                            JsonPointer.compile(instruction.getPath()));
                    break;
                case REMOVE:
                    if (removeHandler == null) {
                        throw new PatchOperationNotSupportedException(PatchOperation.ADD);
                    }

                    removeHandler.remove(JsonPointer.compile(instruction.getPath()));
                    break;
                case REPLACE:
                    if (replaceHandler == null) {
                        throw new PatchOperationNotSupportedException(PatchOperation.ADD);
                    }

                    replaceHandler.replace(JsonPointer.compile(instruction.getPath()), instruction.getValue());
                    break;
                case TEST:
                    if (testHandler == null) {
                        throw new PatchOperationNotSupportedException(PatchOperation.ADD);
                    }

                    testHandler.test(JsonPointer.compile(instruction.getPath()), instruction.getValue());
                    break;
                default:
                    break;
            }
        }
    }

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
