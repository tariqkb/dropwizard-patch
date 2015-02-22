package io.progix.dropwizard.patch.test.jsonpatch;

import org.junit.Test;

import java.io.IOException;

import static io.dropwizard.testing.FixtureHelpers.fixture;
public class JsonPatchTest {

    @Test
    public void test() throws IOException {
        fixture("jsonpatch/tests.json");
    }
}
