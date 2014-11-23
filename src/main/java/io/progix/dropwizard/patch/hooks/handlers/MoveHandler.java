package io.progix.dropwizard.patch.hooks.handlers;

import com.fasterxml.jackson.core.JsonPointer;

public interface MoveHandler {

    public void move(JsonPointer from, JsonPointer path);
}
