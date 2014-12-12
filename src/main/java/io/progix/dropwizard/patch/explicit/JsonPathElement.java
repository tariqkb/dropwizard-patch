package io.progix.dropwizard.patch.explicit;

public class JsonPathElement {

    private Integer value;
    private boolean endOfArray;

    public JsonPathElement(int value) {
        this.value = value;
    }

    public JsonPathElement(boolean endOfArray) {
        this.value = null;
        this.endOfArray = endOfArray;
    }

    public JsonPathElement() {
        this(false);
    }

    public boolean exists() {
        return value != null || endOfArray;
    }

    public int val() {
        return value;
    }

    public boolean is(int value) {
        return this.value == value;
    }

    public boolean isEndOfArray() {
        return this.endOfArray;
    }

}
