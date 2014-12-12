package io.progix.dropwizard.patch.explicit;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;

import java.io.IOException;
import java.util.Arrays;

/**
 * {@link com.fasterxml.jackson.databind.JsonDeserializer} for {@link io.progix.dropwizard.patch.explicit.PatchRequest}
 * <p/>
 * A custom deserializer is needed to convert an array of {@link io.progix.dropwizard.patch.explicit.PatchInstruction}
 * into the {@link io.progix.dropwizard.patch.explicit.PatchRequest} object.
 */
public class PatchRequestDeserializer extends JsonDeserializer<PatchRequest> {

    @Override
    public PatchRequest deserialize(JsonParser jp,
            DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ObjectMapper mapper = Jackson.newObjectMapper();
        PatchInstruction[] instructions = mapper.readValue(jp, PatchInstruction[].class);
        return new PatchRequest(Arrays.asList(instructions));
    }
}
