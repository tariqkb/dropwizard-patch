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

package io.progix.dropwizard.patch;

/**
 * Enum for all patch operations listed in RFC6902. Each operation has comments explaining common usages. For more
 * detailed information, reference RFC6902.
 *
 * @see <a href="https://tools.ietf.org/html/rfc6902"></a>
 */
public enum JsonPatchOperationType {
    /**
     * The patch operation ADD as defined in RFC6902 can be used in three ways.
     * <p/>
     * Adding a value to an array
     * <p/>
     * Adding an non-existent key and value
     * <p/>
     * Overwriting an existent key with a new value
     */
    ADD,

    /**
     * The patch operation REMOVE as defined in RFC6902 can be used in two ways.
     * <p/>
     * Removing a key and its value
     * <p/>
     * Removing an element in an array
     */
    REMOVE,

    /**
     * The patch operation REPLACE as defined in RFC6902 can be used in two ways.
     * <p/>
     * Replacing a value for an object
     * <p/>
     * Replacing an element in an array
     */
    REPLACE,

    /**
     * The patch operation REPLACE as defined in RFC6902 can be used in three ways.
     * <p/>
     * Moving a value of a key to another non-existent key's value, creating that key
     * <p/>
     * Moving a value of a key to another existent key's value
     * <p/>
     * Moving an element in an array to a specified index/key
     */
    MOVE,

    /**
     * The patch operation COPY as defined in RFC6902 can be used in two ways.
     * <p/>
     * Copying a key's value into another key's value
     * <p/>
     * Copying an array element to a key/index
     */
    COPY,

    /**
     * The patch operation COPY as defined in RFC6902 can be used to validate a resource's data.
     * <p/>
     * Test a key/index for a value
     */
    TEST
}
