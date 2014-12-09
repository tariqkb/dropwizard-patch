package io.progix.dropwizard.patch.hooks;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper class for Jackson's com.fasterxml.jackson.core.JsonPath
 */
public class JsonPath {

    private List<String> properties;
    private List<Integer> elements;

    public JsonPath(com.fasterxml.jackson.core.JsonPointer pointer) {
        this.properties = new ArrayList<>();
        this.elements = new ArrayList<>();

        while (pointer != null) {
            if (pointer.mayMatchProperty()) {
                properties.add(pointer.getMatchingProperty());
            } else {
                properties.add(null);
            }

            if (pointer.mayMatchElement()) {
                elements.add(pointer.getMatchingIndex());
            } else {
                elements.add(null);
            }
            pointer = pointer.tail();
        }
    }

    public List<String> getProperties() {
        return properties;
    }

    public String property(int index) {
        return properties.get(index);
    }

    public Integer element(int index) {
        return elements.get(index);
    }
}
