package io.progix.dropwizard.patch.hooks.handlers;

import com.fasterxml.jackson.core.JsonPointer;

public interface RemoveHandler {
    public void remove(JsonPointer path);

    public void removeElement(JsonPointer path, int index);
}
