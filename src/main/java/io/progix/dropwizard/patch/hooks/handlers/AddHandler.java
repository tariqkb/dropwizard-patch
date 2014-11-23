package io.progix.dropwizard.patch.hooks.handlers;

import com.fasterxml.jackson.core.JsonPointer;

/**
 * Serves the handler for the ADD operation. RFC5789 states 3 possible uses of the add operation. Only the case of
 * adding/putting an element within a collection is implemented
 * here because one use mimics the replace operation and the other adds properties to resources which is not possible
 * in Java.
 */
public interface AddHandler {

    public void addElement(JsonPointer path, int index, Object value);

    public void add(JsonPointer path, Object value);
}
