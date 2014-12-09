package io.progix.dropwizard.patch.hooks.handlers;

import io.progix.dropwizard.patch.hooks.JsonPath;

public interface MoveHandler {

    public boolean move(JsonPath from, JsonPath path);
}
