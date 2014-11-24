package io.progix.dropwizard.patch.hooks.handlers;

import com.fasterxml.jackson.core.JsonPointer;

public interface ReplaceHandler {

    public void replace(JsonPointer path, Object value);

    public void replaceElement(JsonPointer path, int index);
}
