package io.progix.dropwizard.patch.hooks.handlers;


import io.progix.dropwizard.patch.hooks.JsonPath;

public interface RemoveHandler {

    public boolean remove(JsonPath path);

}
