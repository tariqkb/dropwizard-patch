package io.progix.dropwizard.patch.explicit;

/**
 * A helper class to wrap segments within {@link io.progix.dropwizard.patch.explicit.JsonPath} that are Integer based,
 * as opposed to String properties.
 *
 * @see io.progix.dropwizard.patch.explicit.JsonPathElement
 */
public class JsonPathElement {

    private Integer value;
    private boolean endOfArray;

    /**
     * @param value the int value for a segment within a {@link io.progix.dropwizard.patch.explicit.JsonPath}
     */
    public JsonPathElement(int value) {
        this.value = value;
    }

    /**
     * Constructs an empty element to signify this segment is not an integer index
     */
    public JsonPathElement() {
        this(false);
    }

    /**
     * @param endOfArray true if this segment is the special character '-' that signifies the end of an array. false if
     *                   an empty element
     */
    public JsonPathElement(boolean endOfArray) {
        this.value = null;
        this.endOfArray = endOfArray;
    }

    /**
     * Checks if this path element is a Integer property. This is useful to determine if this path segment is an integer
     * index as opposed a String
     *
     * @return true if this path segment is an Integer property, false otherwise
     */
    public boolean exists() {
        return value != null || endOfArray;
    }

    /**
     * This usually isn't needed. Use {@link JsonPathElement#is(int)} to check if this property matches an Integer
     *
     * @return the integer value for this element.
     * @throws java.lang.NullPointerException if this element is empty. Always use {@link JsonPathElement#exists()}
     *                                        before this method
     */
    public int val() {
        return value;
    }

    /**
     * Useful for traversing the {@link io.progix.dropwizard.patch.explicit.JsonPath}
     * <p/>
     *
     * @param value the Integer value to compare this element to
     * @return true if equivalent, false otherwise
     */
    public boolean is(int value) {
        return this.value == value;
    }

    /**
     * @return true if this element uses the special character '-' to signify the end of an array, false otherwise
     */
    public boolean isEndOfArray() {
        return this.endOfArray;
    }

}
