package io.progix.dropwizard.patch.test;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import io.progix.dropwizard.patch.BasicJsonPatchDeserializer;
import io.progix.dropwizard.patch.ContextualJsonPatchDeserializer;
import io.progix.dropwizard.patch.DefaultJsonPatch;
import io.progix.dropwizard.patch.DefaultJsonPatchDeserializer;
import io.progix.dropwizard.patch.JsonPatchDeserializerHelper;
import io.progix.jackson.JsonPatchOperation;
import io.progix.jackson.JsonPatchOperationType;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class DefaultJsonPatchTest {

    DefaultJsonPatch<Object> patch;

    @Before
    public void init() {
        ObjectMapper mapper = Jackson.newObjectMapper();
        JsonPatchDeserializerHelper.register(mapper);

        JsonPatchOperation add = new JsonPatchOperation(JsonPatchOperationType.ADD,
                JsonPointer.compile("/"), mapper.convertValue("test", JsonNode.class));

        patch = new DefaultJsonPatch<>(Arrays.asList(add), mapper);
    }

    @Test
    public void testOverride() {
        //TODO
    }
}
