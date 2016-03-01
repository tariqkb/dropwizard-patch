package io.progix.dropwizard.patch;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by dsmith on 3/1/16.
 */
public class JsonPatchDeserializerHelper {
    public static void register(ObjectMapper mapper) {
        new ContextualJsonPatchDeserializer(mapper).register();
        new BasicJsonPatchDeserializer(mapper).register();
        new DefaultJsonPatchDeserializer(mapper).register();
    }
}
