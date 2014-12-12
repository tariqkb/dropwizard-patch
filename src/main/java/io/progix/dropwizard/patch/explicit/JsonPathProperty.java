package io.progix.dropwizard.patch.explicit;

import java.util.Objects;

/**
 * A helper class to wrap segments within {@link io.progix.dropwizard.patch.explicit.JsonPath} that are String based, as
 * opposed to integer indexes.
 *
 * @see io.progix.dropwizard.patch.explicit.JsonPathElement
 */
public class JsonPathProperty {

    private String value;

    /**
     * @param value the String value for a segment within a {@link io.progix.dropwizard.patch.explicit.JsonPath}
     */
    public JsonPathProperty(String value) {
        this.value = value;
    }

    /**
     * Constructs an empty property representing a segment that is not a String property
     */
    public JsonPathProperty() {
        this.value = null;
    }

    /**
     * Checks if this path element is a String property. This is useful to determine if this path segment is a property
     * as opposed a index
     *
     * @return true if this path segment is a String property, false otherwise
     */
    public boolean exists() {
        return value != null;
    }

    /**
     * This usually isn't needed. Use {@link JsonPathProperty#is(String)} to check if this property matches a String
     *
     * @return the String property value. Returns null if an empty property
     */
    public String val() {
        return value;
    }

    /**
     * Useful for traversing the {@link io.progix.dropwizard.patch.explicit.JsonPath}
     * <p/>
     * Uses {@link java.util.Objects#equals(Object, Object)} for null-safe equality
     *
     * @param value the String value to compare this property to
     * @return true if equivalent, false otherwise
     */
    public boolean is(String value) {
        return Objects.equals(this.value, value);
    }
}
