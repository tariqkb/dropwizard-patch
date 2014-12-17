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
 * {@link JsonDeserializer} for {@link PatchRequest}
 * <p/>
 * A custom deserializer is needed to convert an array of {@link PatchInstruction} into the {@link PatchRequest}
 * object.
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
