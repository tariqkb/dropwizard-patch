package io.progix.dropwizard.patch.hooks.handlers;

import io.progix.dropwizard.patch.hooks.JsonPatchValue;
import io.progix.dropwizard.patch.hooks.JsonPath;

import java.util.List;

public interface ReplaceHandler {

    public boolean replace(JsonPath path, JsonPatchValue value);

}
