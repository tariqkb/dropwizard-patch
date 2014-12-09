package io.progix.dropwizard.patch.hooks.handlers;


import io.progix.dropwizard.patch.hooks.JsonPatchValue;
import io.progix.dropwizard.patch.hooks.JsonPath;

import java.util.List;

public interface TestHandler {

    public boolean test(JsonPath path, JsonPatchValue value);

}