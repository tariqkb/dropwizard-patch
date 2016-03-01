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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.progix.jackson.JsonPatchOperation;

import java.io.IOException;
import java.util.Arrays;

/**
 * {@link JsonDeserializer} for {@link ContextualJsonPatch}
 * <p/>
 * A custom deserializer is needed to convert an array of {@link JsonPatchOperation} into the {@link
 * ContextualJsonPatch} object.
 */
public class ContextualJsonPatchDeserializer extends JsonDeserializer<ContextualJsonPatch<?>> {

    private final ObjectMapper mapper;

    public ContextualJsonPatchDeserializer(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public ContextualJsonPatch<?> deserialize(JsonParser jp,
                                              DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonPatchOperation[] instructions = mapper.readValue(jp, JsonPatchOperation[].class);
        return new ContextualJsonPatch<Object>(Arrays.asList(instructions), mapper);
    }

    public void register() {
        final SimpleModule module = new SimpleModule();
        module.addDeserializer(ContextualJsonPatch.class, this);
        mapper.registerModule(module);
    }
}
