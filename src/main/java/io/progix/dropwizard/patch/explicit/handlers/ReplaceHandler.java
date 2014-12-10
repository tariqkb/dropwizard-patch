package io.progix.dropwizard.patch.explicit.handlers;

import io.progix.dropwizard.patch.explicit.JsonPatchValue;
import io.progix.dropwizard.patch.explicit.JsonPath;

public interface ReplaceHandler {

    public boolean replace(JsonPath path, JsonPatchValue value);

}
