package io.progix.dropwizard.patch.explicit;

import com.fasterxml.jackson.core.JsonPointer;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper class for Jackson's {@link com.fasterxml.jackson.core.JsonPointer}
 * <p/>
 * Created to be more user-friendly for explicit patching. This class is used to determine what a String Json path as
 * defined in RFC6901 points to
 */
public class JsonPath {

    private List<JsonPathProperty> properties;
    private List<JsonPathElement> elements;
    private String pathString;

    /**
     * Creates the path using a {@link com.fasterxml.jackson.core.JsonPointer} by iterating through the segments and
     * creating {@link io.progix.dropwizard.patch.explicit.JsonPathProperty} and {@link
     * io.progix.dropwizard.patch.explicit.JsonPathElement} for each segment.
     * <p/>
     * If a given segment does not match as a String property, an empty {@link io.progix.dropwizard.patch.explicit.JsonPathProperty}
     * is created.
     * <p/>
     * If a given segment does not match as a Integer index, an empty {@link io.progix.dropwizard.patch.explicit.JsonPathElement}
     * is created.
     *
     * @param pointer
     */
    public JsonPath(JsonPointer pointer) {
        this.properties = new ArrayList<>();
        this.elements = new ArrayList<>();
        this.pathString = "";

        while (pointer != null) {
            if (pointer.mayMatchProperty() && !pointer.getMatchingProperty().isEmpty()) {
                properties.add(new JsonPathProperty(pointer.getMatchingProperty()));
                this.pathString += pointer.getMatchingProperty() + "/";
            } else if (pointer.getMatchingProperty().equals("-")) {
                /* This character represents the last element in an array */
                elements.add(new JsonPathElement(true));
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

    /**
     * Convenience method to retrieve all {@link io.progix.dropwizard.patch.explicit.JsonPathProperty} for this path
     *
     * @return the list of {@link io.progix.dropwizard.patch.explicit.JsonPathProperty}
     */
    public List<JsonPathProperty> getProperties() {
        return properties;
    }

    /**
     * Convenience method to retrieve all {@link io.progix.dropwizard.patch.explicit.JsonPathElement} for this path
     *
     * @return the list of {@link io.progix.dropwizard.patch.explicit.JsonPathElement}
     */
    public List<JsonPathElement> getElements() {
        return elements;
    }

    /**
     * This method will never return null. If trying to access a {@link io.progix.dropwizard.patch.explicit.JsonPathProperty}
     * for a segment that is not a String property, will return a special object who's {@link JsonPathProperty#exists()}
     * will return false
     *
     * @param index the segment index to retrieve
     * @return a {@link io.progix.dropwizard.patch.explicit.JsonPathProperty} for this index
     */
    public JsonPathProperty property(int index) {
        return properties.get(index);
    }

    /**
     * This method will never return null. If trying to access a {@link io.progix.dropwizard.patch.explicit.JsonPathElement}
     * for a segment that is not an Integer property, will return a special object who's {@link
     * JsonPathElement#exists()} will return false
     *
     * @param index the segment index to retrieve
     * @return a {@link io.progix.dropwizard.patch.explicit.JsonPathElement} for this index
     */
    public JsonPathElement element(int index) {
        return elements.get(index);
    }

    /**
     * This method can be used to determine when an {@link io.progix.dropwizard.patch.InvalidPatchPathException} should
     * be thrown. Uses this exception and method provides useful information for the client when trying to patch a
     * resource in a way the server does not support
     * <p/>
     * Ex. If the path is "/a/b/c", endsAt(2) will return true while endsAt(1) and endsAt(3) will return false
     *
     * @param index The index to test if this path ends
     * @return true if the index provided is the last segment to contain data, false otherwise
     */
    public boolean endsAt(int index) {
        index++;
        return !property(index).exists() && !element(index).exists();
    }

    @Override
    public String toString() {
        return pathString;
    }
}
