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

package io.progix.dropwizard.patch.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import io.progix.dropwizard.patch.ContextualJsonPatch;
import io.progix.dropwizard.patch.JsonPatch;
import io.progix.dropwizard.patch.JsonPatchOperation;
import io.progix.dropwizard.patch.JsonPatchOperationType;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.fest.assertions.api.Assertions.assertThat;

public class JsonPatchSerializationTest {

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    private JsonPatchOperation rp1, rp2, rp3, rp4, rp5, rp6, rp7, rp8, rp9, rp10, rp11, rp12, a1, a2, a3, a4, a5, a6, a7,
            a8, a9, a10, a11, a12, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, m1, m2, m3, m4, m5,
            m6, m7, m8, m9, c1, c2, c3, c4, c5, c6, c7, c8, c9, rm1, rm2, rm3;

    @Before
    public void init() {
        String[] s = new String[]{"1", "2"};
        Integer[] i = new Integer[]{3, 4};

        rp1 = replace("/a/c", "1");
        rp2 = replace("/a/c", 3);
        rp3 = replace("/a/c", s);
        rp4 = replace("/a/c", i);

        rp5 = replace("/a/0", "1");
        rp6 = replace("/a/0", 3);
        rp7 = replace("/a/0", s);
        rp8 = replace("/a/0", i);

        rp9 = replace("/a/-", "1");
        rp10 = replace("/a/-", 3);
        rp11 = replace("/a/-", s);
        rp12 = replace("/a/-", i);

        a1 = add("/a/c", "1");
        a2 = add("/a/c", 3);
        a3 = add("/a/c", s);
        a4 = add("/a/c", i);

        a5 = add("/a/0", "1");
        a6 = add("/a/0", 3);
        a7 = add("/a/0", s);
        a8 = add("/a/0", i);

        a9 = add("/a/-", "1");
        a10 = add("/a/-", 3);
        a11 = add("/a/-", s);
        a12 = add("/a/-", i);

        t1 = test("/a/c", "1");
        t2 = test("/a/c", 3);
        t3 = test("/a/c", s);
        t4 = test("/a/c", i);

        t5 = test("/a/0", "1");
        t6 = test("/a/0", 3);
        t7 = test("/a/0", s);
        t8 = test("/a/0", i);

        t9 = test("/a/-", "1");
        t10 = test("/a/-", 3);
        t11 = test("/a/-", s);
        t12 = test("/a/-", i);

        m1 = move("/a/c", "/a/d");
        m2 = move("/a/c", "/a/d/0");
        m3 = move("/a/c", "/a/d/-");

        m4 = move("/a/0", "/a/d");
        m5 = move("/a/0", "/a/d/0");
        m6 = move("/a/0", "/a/d/-");

        m7 = move("/a/-", "/a/d");
        m8 = move("/a/-", "/a/d/0");
        m9 = move("/a/-", "/a/d/-");

        c1 = copy("/a/c", "/a/d");
        c2 = copy("/a/c", "/a/d/0");
        c3 = copy("/a/c", "/a/d/-");

        c4 = copy("/a/0", "/a/d");
        c5 = copy("/a/0", "/a/d/0");
        c6 = copy("/a/0", "/a/d/-");

        c7 = copy("/a/-", "/a/d");
        c8 = copy("/a/-", "/a/d/0");
        c9 = copy("/a/-", "/a/d/-");

        rm1 = remove("/a/c");
        rm2 = remove("/a/0");
        rm3 = remove("/a/-");
    }

    @Test
    public void jsonPatchDeserialization() throws Exception {
        JsonPatch request = new JsonPatch(
                Arrays.asList(rp1, rp2, rp3, rp4, rp5, rp6, rp7, rp8, rp9, rp10, rp11, rp12, a1, a2, a3, a4, a5, a6, a7,
                        a8, a9, a10, a11, a12, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, m1, m2, m3, m4, m5,
                        m6, m7, m8, m9, c1, c2, c3, c4, c5, c6, c7, c8, c9, rm1, rm2, rm3));

        assertThat(MAPPER.readValue(fixture("fixtures/patchrequest.json"), JsonPatch.class)).isEqualTo(request);
    }

    @Test
    public void contextualJsonPatchDeserialization() throws Exception {
        ContextualJsonPatch<?> contextualRequest = new ContextualJsonPatch<Object>(
                Arrays.asList(rp1, rp2, rp3, rp4, rp5, rp6, rp7, rp8, rp9, rp10, rp11, rp12, a1, a2, a3, a4, a5, a6, a7,
                        a8, a9, a10, a11, a12, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, m1, m2, m3, m4, m5,
                        m6, m7, m8, m9, c1, c2, c3, c4, c5, c6, c7, c8, c9, rm1, rm2, rm3));
        assertThat(MAPPER.readValue(fixture("fixtures/patchrequest.json"), ContextualJsonPatch.class)).isEqualTo(contextualRequest);
    }

    private JsonPatchOperation replace(String path, Object[] values) {
        return new JsonPatchOperation(JsonPatchOperationType.REPLACE, path, new ArrayList<>(Arrays.asList(values)), null);
    }

    private JsonPatchOperation add(String path, Object[] values) {
        return new JsonPatchOperation(JsonPatchOperationType.ADD, path, new ArrayList<>(Arrays.asList(values)), null);
    }

    private JsonPatchOperation test(String path, Object[] values) {
        return new JsonPatchOperation(JsonPatchOperationType.TEST, path, new ArrayList<>(Arrays.asList(values)), null);
    }

    private JsonPatchOperation replace(String path, Object values) {
        return new JsonPatchOperation(JsonPatchOperationType.REPLACE, path, new ArrayList<>(Arrays.asList(values)), null);
    }

    private JsonPatchOperation add(String path, Object values) {
        return new JsonPatchOperation(JsonPatchOperationType.ADD, path, new ArrayList<>(Arrays.asList(values)), null);
    }

    private JsonPatchOperation test(String path, Object values) {
        return new JsonPatchOperation(JsonPatchOperationType.TEST, path, new ArrayList<>(Arrays.asList(values)), null);
    }

    private JsonPatchOperation move(String path, String from) {
        return new JsonPatchOperation(JsonPatchOperationType.MOVE, path, null, from);
    }

    private JsonPatchOperation copy(String path, String from) {
        return new JsonPatchOperation(JsonPatchOperationType.COPY, path, null, from);
    }

    private JsonPatchOperation remove(String path) {
        return new JsonPatchOperation(JsonPatchOperationType.REMOVE, path, null, null);
    }
}
