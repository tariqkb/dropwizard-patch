package io.progix.dropwizard.patch.hooks;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Object representing single operation within a PATCH document as defined in
 * RFC 6902. Uses a custom deserializer to map arbitrary object values within the value property as defined by {@link
 * com.fasterxml.jackson.databind.deser.std.UntypedObjectDeserializer}
 *
 * @author Tariq Bugrara
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

    public List<Object> getValue() {
        return value;
    }

    public String getFrom() {
        return from;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        PatchInstruction that = (PatchInstruction) o;

        if (from != null ? !from.equals(that.from) : that.from != null)
            return false;
        if (operation != that.operation)
            return false;
        if (!path.equals(that.path))
            return false;
        if (value != null ? !value.equals(that.value) : that.value != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = operation.hashCode();
        result = 31 * result + path.hashCode();
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (from != null ? from.hashCode() : 0);
        return result;
    }
}
