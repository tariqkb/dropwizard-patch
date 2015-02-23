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

import com.fasterxml.jackson.databind.JsonNode;
import io.dropwizard.jackson.Jackson;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper class for value passed in as the value key in a JSON Patch document as outlined in RFC6902.
 * <p/>
 * This class uses Jackson to map the JSON objects from the patch document into expected classes
 */
public class JsonPatchValue {

    private JsonNode value;

    /**
     * Constructs an instance using a {@link JsonNode}
     *
     * @param value the {@link JsonNode} representing the value in a patch instruction
     */
    JsonPatchValue(JsonNode value) {
        this.value = value;
    }

    /**
     * This method is used to cast and map the list of objects in this class to a specified known class using Jackson
     *
     * @param clazz The class to cast the elements of this list to
     * @param <T>   The class type of the class param
     *
     * @return A list of value casted and mapped to the specified class
     */
    public <T> List<T> many(Class<T> clazz) {
        List<T> mappedValues = new ArrayList<>();
        if (value.isArray()) {
            for (JsonNode valueElement : value) {
                if (valueElement == null) {
                    mappedValues.add(null);
                }
                mappedValues.add(Jackson.newObjectMapper().convertValue(valueElement, clazz));
            }
        } else {
            mappedValues.add(Jackson.newObjectMapper().convertValue(value, clazz));
        }
        return mappedValues;
    }

    /**
     * This method is used to cast and map the list of objects in this class to a specified known class using Jackson
     *
     * @param clazz The class to cast the single element of this list to
     * @param <T>   The class type of the class param
     *
     * @return A single value casted and mapped to the specified class.
     *
     * @throws IndexOutOfBoundsException if there is more than one element in the list or if the list is empty.
     */
    public <T> T one(Class<T> clazz) {
        if (value.isArray()) {
            throw new UnsupportedOperationException("Cannot convert a list of values into one. See many(Class");
        }
        return Jackson.newObjectMapper().convertValue(value, clazz);
    }

    public JsonNode getNode() {
        return value;
    }
}