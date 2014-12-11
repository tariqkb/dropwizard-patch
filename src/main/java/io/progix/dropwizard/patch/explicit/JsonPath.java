package io.progix.dropwizard.patch.explicit;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper class for Jackson's com.fasterxml.jackson.core.JsonPath
 */
public class JsonPath {

    private List<JsonPathProperty> properties;
    private List<JsonPathElement> elements;
    private String pathString;

    public JsonPath(com.fasterxml.jackson.core.JsonPointer pointer) {
        this.properties = new ArrayList<>();
        this.elements = new ArrayList<>();
        this.pathString = "";

        while (pointer != null) {
            if (pointer.mayMatchProperty() && !pointer.getMatchingProperty().isEmpty()) {
                properties.add(new JsonPathProperty(pointer.getMatchingProperty()));
                this.pathString += pointer.getMatchingProperty() + "/";
            } else {
                properties.add(new JsonPathProperty());
            }

            if (pointer.mayMatchElement()) {
                elements.add(new JsonPathElement(pointer.getMatchingIndex()));
                this.pathString += pointer.getMatchingIndex() + "/";
            } else {
                elements.add(new JsonPathElement());
            }
            pointer = pointer.tail();
        }

        this.pathString = this.pathString.substring(0, this.pathString.length() - 1);
    }

    public List<JsonPathProperty> getProperties() {
        return properties;
    }

    public List<JsonPathElement> getElements() {
        return elements;
    }

    public JsonPathProperty property(int index) {
        return properties.get(index);
    }

    public JsonPathElement element(int index) {
        return elements.get(index);
    }

    public boolean endsAt(int index) {
        index++;
        return !property(index).exists() && !element(index).exists();
    }

    @Override
    public String toString() {
        return pathString;
    }
}
