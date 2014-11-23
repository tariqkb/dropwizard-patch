package io.progix.dropwizard.patch.hooks.handlers;

import com.fasterxml.jackson.core.JsonPointer;

public interface CopyHandler {

    public void copy(JsonPointer from, JsonPointer path);
}
