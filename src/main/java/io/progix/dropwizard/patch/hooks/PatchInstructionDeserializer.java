package io.progix.dropwizard.patch.hooks;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PatchInstructionDeserializer extends JsonDeserializer<PatchInstruction> {

    @Override
    public PatchInstruction deserialize(JsonParser jp,
            DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        PatchOperation operation = PatchOperation.valueOf(node.get("op").asText());
        String path = node.get("path").asText();
        String from = node.get("from").asText();
        List<Object> values = new ArrayList<Object>();
        JsonNode valueNode = node.get("value");
        if(valueNode.isArray()) {
            Iterator<JsonNode> iterator = valueNode.elements();
            while(iterator.hasNext()) {
                values.add(iterator.next().as);
            }
        } else {

        }
    }
}
