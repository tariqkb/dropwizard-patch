package io.progix.dropwizard.patch.explicit.handlers;

import io.progix.dropwizard.patch.explicit.JsonPath;

public interface CopyHandler {

    public void copy(JsonPath from, JsonPath path);
}
