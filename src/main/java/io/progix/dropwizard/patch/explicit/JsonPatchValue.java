package io.progix.dropwizard.patch.explicit;

import io.dropwizard.jackson.Jackson;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper class for values passed in as the value key in a JSON Patch document as outlined in RFC6902.
 *
 * This class uses Jackson to map the JSON objects from the patch document into expected classes
 */
public class JsonPatchValue {
    private List<? extends Object> values;

    /**
     * Constructs an instance using a list of values
     *
     * @param values the values this {@link io.progix.dropwizard.patch.explicit.JsonPatchValue} uses
     */
    public JsonPatchValue(List<? extends Object> values) {
        this.values = values;
    }

    /**
     * This method is used to cast and map the list of objects in this class to a specified known class using Jackson
     *
     * @param clazz The class to cast the elements of this list to
     * @param <T>   The class type of the class param
     * @return A list of values casted and mapped to the specified class
     */
    public <T> List<T> many(Class<T> clazz) {
        List<T> mappedValues = new ArrayList<>();
        for (Object o : values) {
            if (o == null) {
                mappedValues.add(null);
            }
            mappedValues.add(Jackson.newObjectMapper().convertValue(o, clazz));
        }
        return mappedValues;
    }

    /**
     * This method is used to cast and map the list of objects in this class to a specified known class using Jackson
     *
     * @param clazz The class to cast the single element of this list to
     * @param <T>   The class type of the class param
     * @return A single value casted and mapped to the specified class.
     * @throws java.lang.IndexOutOfBoundsException if there is more than one element in the list or if the list is
     *                                             empty.
     */
    public <T> T one(Class<T> clazz) {
        if (values.get(0) == null) {
            return null;
        }
        return Jackson.newObjectMapper().convertValue(values.get(0), clazz);
    }

}