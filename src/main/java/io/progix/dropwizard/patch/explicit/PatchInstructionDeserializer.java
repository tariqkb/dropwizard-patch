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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.UntypedObjectDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Custom Jackson deserializer for patch operations in a patch document.
 *
 * @see JsonDeserializer
 */
public class PatchInstructionDeserializer extends JsonDeserializer<PatchInstruction> {

    private UntypedObjectDeserializer untypedObjectDeserializer;

    public PatchInstructionDeserializer() {
        untypedObjectDeserializer = new UntypedObjectDeserializer();
    }

    /**
     * This method is responsible for deserialization of a {@link PatchInstruction}
     * <p/>
     * The value in a patch instruction is mapped to a TreeMap and can be later converted to a class of choice using
     * {@link JsonPatchValue}
     *
     * @see JsonDeserializer
     */
    @Override
    public PatchInstruction deserialize(JsonParser jp,
            DeserializationContext ctxt) throws IOException, JsonProcessingException {
        untypedObjectDeserializer.resolve(ctxt);

        JsonNode node = jp.getCodec().readTree(jp);

        PatchOperation operation = PatchOperation.valueOf(node.get("op").asText().toUpperCase());

        String path = node.get("path").asText();

        String from = null;
        JsonNode fromNode = node.get("from");
        if (fromNode != null) {
            from = fromNode.asText();
        }

        List<Object> values = null;
        JsonNode valueNode = node.get("value");
        if (valueNode != null) {
            values = new ArrayList<>();
            if (valueNode.isArray()) {
                Iterator<JsonNode> iterator = valueNode.elements();
                while (iterator.hasNext()) {
                    JsonNode elementNode = iterator.next();
                    JsonParser p = elementNode.traverse();
                    if (!p.hasCurrentToken()) {
                        p.nextToken();
                    }
                    Object element = untypedObjectDeserializer.deserialize(p, ctxt);
                    values.add(element);
                }
            } else {
                JsonParser p = valueNode.traverse();
                if (!p.hasCurrentToken()) {
                    p.nextToken();
                }
                Object value = untypedObjectDeserializer.deserialize(p, ctxt);
                values.add(value);
            }
        }

        return new PatchInstruction(operation, path, values, from);
    }
}
