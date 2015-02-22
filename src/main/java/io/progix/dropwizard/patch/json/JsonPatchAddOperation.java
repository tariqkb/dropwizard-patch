package io.progix.dropwizard.patch.json;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.JsonNode;

public class JsonPatchAddOperation {
    private JsonPointer path;
    private JsonNode value;

    public JsonPatchAddOperation(JsonPointer path, JsonNode value) {
        this.path = path;
        this.value = value;
    }

    public JsonNode patch(JsonNode document) {


    }
}
