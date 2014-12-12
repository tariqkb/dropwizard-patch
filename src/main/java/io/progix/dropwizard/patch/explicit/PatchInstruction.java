package io.progix.dropwizard.patch.explicit;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Object representing single operation within a PATCH document as defined in RFC6902.
 * <p/>
 * Not all properties of this class are not nullable, as the path and value keys in a patch instruction are not always
 * present depending on the patch operation.
 * <p/>
 * <p/>
 * An array of PatchInstruction is deserialized into a {@link io.progix.dropwizard.patch.explicit.PatchRequest}
 * <p/>
 * Note that the operation key should be "op" in the JSON document for proper deserialization
 *
 * @see <a href="https://tools.ietf.org/html/rfc6902"></a>
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

    /**
     * @return the {@link io.progix.dropwizard.patch.explicit.PatchOperation} this instruction uses
     */
    public PatchOperation getOperation() {
        return operation;
    }

    /**
     * @return the String path this instruction references
     */
    public String getPath() {
        return path;
    }

    /**
     * Note this list can be empty and null
     *
     * @return a List of values this instruction may contain
     */
    public List<Object> getValue() {
        return value;
    }

    /**
     * Note this String can be null
     *
     * @return the String path this instruction references as the from key
     */
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
