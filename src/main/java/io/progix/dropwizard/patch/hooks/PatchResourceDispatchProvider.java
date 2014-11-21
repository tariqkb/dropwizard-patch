package io.progix.dropwizard.patch.hooks;

import com.google.common.base.Preconditions;
import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.api.model.AbstractResourceMethod;
import com.sun.jersey.spi.container.ResourceMethodDispatchProvider;
import com.sun.jersey.spi.dispatch.RequestDispatcher;
import io.dropwizard.jersey.PATCH;

public class PatchResourceDispatchProvider implements ResourceMethodDispatchProvider {

    public static class PatchHookDispatcher implements RequestDispatcher {

        private final RequestDispatcher dispatcher;
        private final PatchRequest request;

        public PatchHookDispatcher(RequestDispatcher dispatcher, PatchRequest request) {
            this.dispatcher = dispatcher;
            this.request = request;
        }

        @Override
        public void dispatch(Object resource, HttpContext context) {
            try {
                dispatcher.dispatch(resource, context);
            } finally {

            }
        }
    }

    private final ResourceMethodDispatchProvider provider;

    public PatchResourceDispatchProvider(ResourceMethodDispatchProvider provider) {
        this.provider = Preconditions.checkNotNull(provider);
    }

    @Override
    public RequestDispatcher create(AbstractResourceMethod method) {
        RequestDispatcher dispatcher = provider.create(method);
        if (dispatcher == null) {
            return null;
        }

        if (method.getMethod().isAnnotationPresent(PATCH.class)) {
            dispatcher = new PatchHookDispatcher();
        }

        return dispatcher;
    }
}
