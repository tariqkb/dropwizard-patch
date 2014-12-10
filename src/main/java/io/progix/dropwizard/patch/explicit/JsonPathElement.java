package io.progix.dropwizard.patch.explicit;

public class JsonPathElement {

    private Integer value;

    public JsonPathElement(int value) {
        this.value = value;
    }

    public JsonPathElement() {
        this.value = null;
    }

    public boolean exists() {
        return value != null;
    }

    public int val() {
        return value;
    }

    public boolean is(int value) {
        return this.value == value;
    }

}
