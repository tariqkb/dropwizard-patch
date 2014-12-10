package io.progix.dropwizard.patch.explicit.handlers;


import io.progix.dropwizard.patch.explicit.JsonPatchValue;
import io.progix.dropwizard.patch.explicit.JsonPath;

public interface TestHandler {

    public boolean test(JsonPath path, JsonPatchValue value);

}