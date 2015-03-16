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

package io.progix.dropwizard.patch.operations.contextual;

import io.progix.dropwizard.patch.JsonPath;
import io.progix.dropwizard.patch.operations.MoveOperation;

/**
 * @param <T> the type of the object this move operation should be preformed on
 * @see MoveOperation
 */
public interface ContextualMoveOperation<T> {

    /**
     * @param context The context this move operation should be preformed on
     * @param from    the {@link JsonPath} for the location of the value to be moved.
     * @param path    the {@link JsonPath} for the location the value should be moved to.
     * @see MoveOperation
     */
    public T move(T context, JsonPath from, JsonPath path);
}
