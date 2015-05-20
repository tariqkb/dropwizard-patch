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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.testing.FixtureHelpers;
import io.progix.dropwizard.patch.DefaultJsonPatch;
import io.progix.dropwizard.patch.JsonTestCase;
import io.progix.jackson.JsonPatchOperation;
import io.progix.jackson.exceptions.JsonPatchFailedException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@RunWith(Parameterized.class)
public class SpecTests {

    private final JsonTestCase testCase;
    private final ObjectMapper mapper = new ObjectMapper();

    public SpecTests(JsonTestCase testCase) {
        this.testCase = testCase;
    }

    @Test
    public void testCase() throws IOException {
        if (testCase.isDisabled()) {
            return;
        }

        try {
            JsonNode documentNode = mapper.readTree(testCase.getDoc());

            JsonPatchOperation[] patchOperations = mapper.convertValue(testCase.getPatch(), JsonPatchOperation[].class);
            DefaultJsonPatch<JsonNode>  patch = new DefaultJsonPatch<>(Arrays.asList(patchOperations));

            JsonNode resultNode = patch.apply(documentNode);
            if (testCase.isErrorCase()) {
                fail("Expected error, but there was none: " + testCase.getError());
            }

            //If not, just a test to make sure no exceptions
            if (testCase.getExpected() != null) {
                JsonNode expectedNode = mapper.readTree(testCase.getExpected());

                assertThat(resultNode).isEqualTo(expectedNode);
            }

        } catch (JsonPatchFailedException | IllegalArgumentException e) {
            if (!testCase.isErrorCase()) {
                fail("Did not expect error but got one", e);
            }
        }
    }

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        List<Object[]> datas = new ArrayList<Object[]>();

        ObjectMapper mapper = new ObjectMapper();
        JsonTestCase[] testCases;

        try {
            testCases = mapper
                    .readValue(FixtureHelpers.fixture("fixtures/jsonpatch/tests.json"), JsonTestCase[].class);

            for (JsonTestCase testCase : testCases) {
                datas.add(new Object[]{testCase});
            }

            return datas;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
