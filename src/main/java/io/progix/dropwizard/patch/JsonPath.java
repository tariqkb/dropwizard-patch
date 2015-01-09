/*
 * Copyright 2014 Tariq Bugrara
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.progix.dropwizard.patch;

import com.fasterxml.jackson.core.JsonPointer;
import io.progix.dropwizard.patch.exception.InvalidPatchPathException;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper class for Jackson's {@link JsonPointer}
 * <p/>
 * Created to be more user-friendly for explicit patching. This class is used to determine what a String Json path as
 * defined in RFC6901 points to
 */
public class JsonPath {

    private List<JsonPathProperty> properties;
    private List<JsonPathElement> elements;
    private String pathString;

    /**
     * Creates the path using a {@link JsonPointer} by iterating through the segments and creating {@link
     * JsonPathProperty} and {@link JsonPathElement} for each segment.
     * <p/>
     * If a given segment does not match as a String property, an empty {@link JsonPathProperty} is created.
     * <p/>
     * If a given segment does not match as a Integer index, an empty {@link JsonPathElement} is created.
     *
     * @param pointer
     * @see com.fasterxml.jackson.core.JsonPointer
     */
    public JsonPath(JsonPointer pointer) {
        this.properties = new ArrayList<>();
        this.elements = new ArrayList<>();
        this.pathString = "";

        while (pointer != null) {

            //  Keep in mind, Jackson's implementation of JsonPointer allows all segments to be properties
            if (pointer.mayMatchProperty() && !pointer.mayMatchElement() && !pointer.getMatchingProperty().isEmpty() && !pointer.getMatchingProperty().equals("-")) {
                properties.add(new JsonPathProperty(pointer.getMatchingProperty()));

                this.pathString += "/" + pointer.getMatchingProperty();
            } else if (pointer.getMatchingProperty().equals("-")) {

                //  This character represents the last element in an array
                elements.add(new JsonPathElement(true));

                this.pathString += pointer.getMatchingProperty() + "/";
            } else {
                properties.add(new JsonPathProperty());
            }

            if (pointer.mayMatchElement()) {
                elements.add(new JsonPathElement(pointer.getMatchingIndex()));

                this.pathString += "/" + pointer.getMatchingIndex();
            } else {
                elements.add(new JsonPathElement());
            }
            pointer = pointer.tail();
        }

    }

    /**
     * Convenience method to retrieve all {@link JsonPathProperty} for this path
     *
     * @return the list of {@link JsonPathProperty}
     */
    public List<JsonPathProperty> getProperties() {
        return properties;
    }

    /**
     * Convenience method to retrieve all {@link JsonPathElement} for this path
     *
     * @return the list of {@link JsonPathElement}
     */
    public List<JsonPathElement> getElements() {
        return elements;
    }

    /**
     * Determines if this JsonPath is equivalent to the given String path.
     * <p/>
     * Note that there should be no leading slash in the given path.
     * <p/>
     * This method treats the string "#" as a special meaning to signify a numerical segment within the JsonPointer. If
     * you use the '#' character as a property name, use "~#" to properly treat that segment as a property and not an
     * array element.
     *
     * @param path String path to check
     * @return true if the path is given is equivalent to this JsonPath, false otherwise.
     */
    public boolean is(String path) {
        if (path.charAt(0) != '/') {
            throw new IllegalArgumentException("Paths must start with a '/'");
        }

        //  Remove first slash which is guaranteed
        path = path.substring(1, path.length());

        String[] tokens = path.split("/");
        if (!endsAt(tokens.length - 1)) {
            return false;
        }

        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];

            if (property(i).exists()) {

                //  Can't be true because a property exists at i and path is checking for an element
                if (token.equals("#")) {
                    return false;
                }

                //  Convert special character to "#"
                if (token.equals("~#")) {
                    token = "#";
                }

                if (!token.equals(property(i).val())) {
                    return false;
                }
            } else if (element(i).exists()) {
                if (token.equals("#") || (token.equals("-") && element(i).isEndOfArray()) || String.valueOf(element(i).val()).equals(token)) {
                    return true;
                }
            } else {
                return false;
            }
        }

        return true;
    }

    /**
     * This method can be used to determine when an {@link InvalidPatchPathException} should be thrown. Uses this
     * exception and method provides useful information for the client when trying to patch a resource in a way the
     * server does not support
     * <p/>
     * Ex. If the path is "/a/b/c", endsAt(2) will return true while endsAt(1) and endsAt(3) will return false
     *
     * @param index The index to test if this path ends
     * @return true if the index provided is the last segment to contain data, false otherwise
     */
    public boolean endsAt(int index) {
        int next = index + 1;
        return (property(index).exists() || element(index).exists()) && !property(next).exists() && !element(next).exists();
    }

    /**
     * This method will never return null. If trying to access a {@link JsonPathProperty} for a segment that is not a
     * String property, will return a special object who's {@link JsonPathProperty#exists()} will return false
     *
     * @param index the segment index to retrieve
     * @return a {@link JsonPathProperty} for this index
     */
    public JsonPathProperty property(int index) {
        if (index >= properties.size()) {
            return new JsonPathProperty();
        }
        return properties.get(index);
    }

    /**
     * This method will never return null. If trying to access a {@link JsonPathElement} for a segment that is not an
     * Integer property, will return a special object who's {@link JsonPathElement#exists()} will return false
     *
     * @param index the segment index to retrieve
     * @return a {@link JsonPathElement} for this index
     */
    public JsonPathElement element(int index) {
        if (index >= elements.size()) {
            return new JsonPathElement();
        }
        return elements.get(index);
    }

    @Override
    public String toString() {
        return pathString;
    }
}
