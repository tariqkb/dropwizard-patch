package io.progix.dropwizard.patch.explicit.handlers;


import io.progix.dropwizard.patch.explicit.JsonPath;

public interface RemoveHandler {

    public boolean remove(JsonPath path);

}
