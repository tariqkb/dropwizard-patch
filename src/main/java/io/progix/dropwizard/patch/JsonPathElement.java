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

/**
 * A helper class to wrap segments within {@link JsonPath} that are Integer based, as opposed to String properties.
 *
 * @see JsonPathElement
 */
public class JsonPathElement {

    private Integer value;
    private boolean endOfArray;

    /**
     * @param value the int value for a segment within a {@link JsonPath}
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
     * <p/>
     *
     * @return the integer value for this element.
     * @throws NullPointerException if this element is empty or represents the end of an array. Always use {@link
     *                              JsonPathElement#exists()} before this method
     */
    public int val() {
        return value;
    }

    /**
     * Useful for traversing the {@link JsonPath}
     * <p/>
     *
     * @param value the Integer value to compare this element to
     * @return true if equivalent, false otherwise
     */
    public boolean is(int value) {
        return this.value != null && this.value == value;

    }

    /**
     * @return true if this element uses the special character '-' to signify the end of an array, false otherwise
     */
    public boolean isEndOfArray() {
        return this.endOfArray;
    }

}
