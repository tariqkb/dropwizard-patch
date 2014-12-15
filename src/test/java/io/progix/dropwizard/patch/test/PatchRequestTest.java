package io.progix.dropwizard.patch.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import io.progix.dropwizard.patch.explicit.PatchInstruction;
import io.progix.dropwizard.patch.explicit.PatchOperation;
import io.progix.dropwizard.patch.explicit.PatchRequest;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.fest.assertions.api.Assertions.assertThat;

public class PatchRequestTest {

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    @Test
    public void deserializesFromJSON() throws Exception {
        String[] s = new String[]{"1", "2"};
        Integer[] i = new Integer[]{3, 4};

        PatchInstruction rp1 = replace("/a/c", "1");
        PatchInstruction rp2 = replace("/a/c", 3);
        PatchInstruction rp3 = replace("/a/c", s);
        PatchInstruction rp4 = replace("/a/c", i);

        PatchInstruction rp5 = replace("/a/0", "1");
        PatchInstruction rp6 = replace("/a/0", 3);
        PatchInstruction rp7 = replace("/a/0", s);
        PatchInstruction rp8 = replace("/a/0", i);

        PatchInstruction rp9 = replace("/a/-", "1");
        PatchInstruction rp10 = replace("/a/-", 3);
        PatchInstruction rp11 = replace("/a/-", s);
        PatchInstruction rp12 = replace("/a/-", i);

        PatchInstruction a1 = add("/a/c", "1");
        PatchInstruction a2 = add("/a/c", 3);
        PatchInstruction a3 = add("/a/c", s);
        PatchInstruction a4 = add("/a/c", i);

        PatchInstruction a5 = add("/a/0", "1");
        PatchInstruction a6 = add("/a/0", 3);
        PatchInstruction a7 = add("/a/0", s);
        PatchInstruction a8 = add("/a/0", i);

        PatchInstruction a9 = add("/a/-", "1");
        PatchInstruction a10 = add("/a/-", 3);
        PatchInstruction a11 = add("/a/-", s);
        PatchInstruction a12 = add("/a/-", i);

        PatchInstruction t1 = test("/a/c", "1");
        PatchInstruction t2 = test("/a/c", 3);
        PatchInstruction t3 = test("/a/c", s);
        PatchInstruction t4 = test("/a/c", i);

        PatchInstruction t5 = test("/a/0", "1");
        PatchInstruction t6 = test("/a/0", 3);
        PatchInstruction t7 = test("/a/0", s);
        PatchInstruction t8 = test("/a/0", i);

        PatchInstruction t9 = test("/a/-", "1");
        PatchInstruction t10 = test("/a/-", 3);
        PatchInstruction t11 = test("/a/-", s);
        PatchInstruction t12 = test("/a/-", i);

        PatchInstruction m1 = move("/a/c", "/a/d");
        PatchInstruction m2 = move("/a/c", "/a/d/0");
        PatchInstruction m3 = move("/a/c", "/a/d/-");

        PatchInstruction m4 = move("/a/0", "/a/d");
        PatchInstruction m5 = move("/a/0", "/a/d/0");
        PatchInstruction m6 = move("/a/0", "/a/d/-");

        PatchInstruction m7 = move("/a/-", "/a/d");
        PatchInstruction m8 = move("/a/-", "/a/d/0");
        PatchInstruction m9 = move("/a/-", "/a/d/-");

        PatchInstruction c1 = copy("/a/c", "/a/d");
        PatchInstruction c2 = copy("/a/c", "/a/d/0");
        PatchInstruction c3 = copy("/a/c", "/a/d/-");

        PatchInstruction c4 = copy("/a/0", "/a/d");
        PatchInstruction c5 = copy("/a/0", "/a/d/0");
        PatchInstruction c6 = copy("/a/0", "/a/d/-");

        PatchInstruction c7 = copy("/a/-", "/a/d");
        PatchInstruction c8 = copy("/a/-", "/a/d/0");
        PatchInstruction c9 = copy("/a/-", "/a/d/-");

        PatchInstruction rm1 = remove("/a/c");
        PatchInstruction rm2 = remove("/a/0");
        PatchInstruction rm3 = remove("/a/-");

        PatchRequest request = new PatchRequest(Arrays.asList(rp1, rp2, rp3, rp4, rp5, rp6, rp7, rp8, rp9, rp10, rp11,
                rp12, a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11,
                t12, m1, m2, m3, m4, m5, m6, m7, m8, m9, c1, c2, c3, c4, c5, c6, c7, c8, c9, rm1, rm2, rm3));

        assertThat(MAPPER.readValue(fixture("fixtures/patchrequest.json"), PatchRequest.class)).isEqualTo(request);
    }

    private PatchInstruction replace(String path, Object[] values) {
        return new PatchInstruction(PatchOperation.REPLACE, path, new ArrayList<>(Arrays.asList(values)), null);
    }

    private PatchInstruction add(String path, Object[] values) {
        return new PatchInstruction(PatchOperation.ADD, path, new ArrayList<>(Arrays.asList(values)), null);
    }

    private PatchInstruction test(String path, Object[] values) {
        return new PatchInstruction(PatchOperation.TEST, path, new ArrayList<>(Arrays.asList(values)), null);
    }

    private PatchInstruction replace(String path, Object values) {
        return new PatchInstruction(PatchOperation.REPLACE, path, new ArrayList<>(Arrays.asList(values)), null);
    }

    private PatchInstruction add(String path, Object values) {
        return new PatchInstruction(PatchOperation.ADD, path, new ArrayList<>(Arrays.asList(values)), null);
    }

    private PatchInstruction test(String path, Object values) {
        return new PatchInstruction(PatchOperation.TEST, path, new ArrayList<>(Arrays.asList(values)), null);
    }

    private PatchInstruction move(String path, String from) {
        return new PatchInstruction(PatchOperation.MOVE, path, null, from);
    }

    private PatchInstruction copy(String path, String from) {
        return new PatchInstruction(PatchOperation.COPY, path, null, from);
    }

    private PatchInstruction remove(String path) {
        return new PatchInstruction(PatchOperation.REMOVE, path, null, null);
    }
}
