package io.progix.dropwizard.patch.explicit;

import io.dropwizard.jackson.Jackson;

import java.util.ArrayList;
import java.util.List;

public class JsonPatchValue {
    private List<? extends Object> values;

    public JsonPatchValue(List<? extends Object> values) {
        this.values = values;
    }

    public <T> List<T> to(Class<T> clazz) {
        List<T> mappedValues = new ArrayList<>();
        for(Object o : values) {
            mappedValues.add(Jackson.newObjectMapper().convertValue(o, clazz));
        }
        return mappedValues;
    }

    public <T> T one(Class<T> clazz) {
        return Jackson.newObjectMapper().convertValue(values.get(0), clazz);
    }

}