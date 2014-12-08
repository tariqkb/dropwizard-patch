package io.progix.dropwizard.patch.hooks.handlers;

import com.fasterxml.jackson.core.JsonPointer;

public interface TestHandler {

    public void test(JsonPointer path, Object value);

}