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

import java.util.Objects;

/**
 * A helper class to wrap segments within {@link JsonPath} that are String based, as opposed to integer indexes.
 *
 * @see JsonPathElement
 */
public class JsonPathProperty {

    private String value;

    /**
     * @param value the String value for a segment within a {@link JsonPath}
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
     * Useful for traversing the {@link JsonPath}
     * <p/>
     * Uses {@link Objects#equals(Object, Object)} for null-safe equality
     *
     * @param value the String value to compare this property to
     *
     * @return true if equivalent, false otherwise
     */
    public boolean is(String value) {
        return Objects.equals(this.value, value);
    }
}
