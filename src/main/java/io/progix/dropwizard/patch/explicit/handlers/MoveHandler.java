package io.progix.dropwizard.patch.explicit.handlers;

import io.progix.dropwizard.patch.explicit.JsonPath;

public interface MoveHandler {

    public void move(JsonPath from, JsonPath path);
}
