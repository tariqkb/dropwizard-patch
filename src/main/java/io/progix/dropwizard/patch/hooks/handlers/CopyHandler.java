package io.progix.dropwizard.patch.hooks.handlers;

import io.progix.dropwizard.patch.hooks.JsonPath;

public interface CopyHandler {

    public boolean copy(JsonPath from, JsonPath path);
}
