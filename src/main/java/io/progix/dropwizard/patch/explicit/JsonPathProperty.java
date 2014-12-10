package io.progix.dropwizard.patch.explicit;

public class JsonPathProperty {

    private String value;

    public JsonPathProperty(String value) {
        this.value = value;
    }

    public JsonPathProperty() {
        this.value = null;
    }

    public boolean exists() {
        return value != null;
    }

    public String val() {
        return value;
    }

    public boolean is(String value) {
        return this.value.equals(value);
    }
}
