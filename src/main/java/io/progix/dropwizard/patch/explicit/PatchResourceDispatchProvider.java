/*
 * Copyright 2014 Tariq Bugrara
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.progix.dropwizard.patch.explicit;

import com.google.common.base.Preconditions;
import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.api.model.AbstractResourceMethod;
import com.sun.jersey.spi.container.ResourceMethodDispatchProvider;
import com.sun.jersey.spi.dispatch.RequestDispatcher;
import io.dropwizard.jersey.PATCH;

public class PatchResourceDispatchProvider implements ResourceMethodDispatchProvider {

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
            //			dispatcher = new PatchHookDispatcher();
        }

        return dispatcher;
    }

    public static class PatchHookDispatcher implements RequestDispatcher {

        private final RequestDispatcher dispatcher;
        private final PatchInstruction request;

        public PatchHookDispatcher(RequestDispatcher dispatcher, PatchInstruction request) {
            this.dispatcher = dispatcher;
            this.request = request;
        }

        @Override
        public void dispatch(Object resource, HttpContext context) {
            try {
                dispatcher.dispatch(resource, context);
            } finally {
                // get patchrequest and call its do function
            }
        }
    }
}
