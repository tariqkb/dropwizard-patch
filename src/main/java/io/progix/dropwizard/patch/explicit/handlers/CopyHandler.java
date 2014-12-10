package io.progix.dropwizard.patch.explicit.handlers;

import io.progix.dropwizard.patch.explicit.JsonPath;

public interface CopyHandler {

    public boolean copy(JsonPath from, JsonPath path);
}
