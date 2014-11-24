package io.progix.dropwizard.patch.hooks;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.UntypedObjectDeserializer;
import io.dropwizard.jackson.Jackson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Custom Jackson deserializer for PATCH operations in a PATCH document.
 *
 * @author Tariq Bugrara
 */
public class PatchInstructionDeserializer extends JsonDeserializer<PatchInstruction> {

    private UntypedObjectDeserializer untypedObjectDeserializer;

    public PatchInstructionDeserializer() {
        untypedObjectDeserializer = new UntypedObjectDeserializer();
    }

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
            values = new ArrayList<Object>();
            if (valueNode.isArray()) {
                Iterator<JsonNode> iterator = valueNode.elements();
                while (iterator.hasNext()) {
                    JsonNode elementNode = iterator.next();
                    JsonParser p = valueNode.traverse();
                    if(!p.hasCurrentToken()) {
                        p.nextToken();
                    }
                    Object element = untypedObjectDeserializer.deserialize(p, ctxt);
                    values.add(element);
                }
            } else {
                JsonParser p = valueNode.traverse();
                if(!p.hasCurrentToken()) {
                    p.nextToken();
                }
                Object value = untypedObjectDeserializer.deserialize(p, ctxt);
                values.add(value);
            }
        }

        return new PatchInstruction(operation, path, values, from);
    }
}
