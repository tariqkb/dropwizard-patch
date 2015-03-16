package io.progix.dropwizard.patch;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;

import java.io.IOException;

public class PatchUtil {

    public static final ObjectMapper mapper = Jackson.newObjectMapper();

    public static <T> T copy(T object) {
        if(object instanceof JsonNode) {
            return (T) ((JsonNode) object).deepCopy();
        }

        try {
            return mapper.readValue(mapper.writeValueAsString(object),  mapper.getTypeFactory().constructType(object.getClass()));
        } catch (IOException e) {
            throw new RuntimeException("An error occurred while copying an object. See stack trace for more information.", e);
        }
    }
}
