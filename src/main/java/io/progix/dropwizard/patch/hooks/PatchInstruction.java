package io.progix.dropwizard.patch.hooks;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * POJO representing single operation a PATCH request can contain as defined in
 * RFC 6902.
 *
 * @author tariq
 */
@JsonDeserialize(using = PatchInstructionDeserializer.class)
public class PatchInstruction {

    @JsonProperty("op")
    @NotNull
    private PatchOperation operation;

    @NotNull
    private String path;
    private List<Object> value;
    private String from;

    public PatchInstruction(PatchOperation operation, String path, List<Object> value, String from) {
        this.operation = operation;
        this.path = path;
        this.value = value;
        this.from = from;
    }

    public PatchOperation getOperation() {
        return operation;
    }

    public String getPath() {
        return path;
    }

    public Object getValue() {
        return value;
    }

    public String getFrom() {
        return from;
    }

    public void setOperation(PatchOperation operation) {
        this.operation = operation;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setValue(List<Object> value) {
        this.value = value;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
