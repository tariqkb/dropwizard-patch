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
        String one = "1";
        Integer three = 3;
        String[] s = new String[] {"1", "2"};
        Integer[] i = new Integer[] {3, 4};

        PatchInstruction r1 = replace("/a/c", "1");
        PatchInstruction r2 = replace("/a/c", 3);
        PatchInstruction r3 = replace("/a/c", s);
        PatchInstruction r4 = replace("/a/c", i);

        PatchInstruction r5 = replace("/a/0", "1");
        PatchInstruction r6 = replace("/a/0", "1");
        PatchInstruction r7 = replace("/a/0", "1");
        PatchInstruction r8 = replace("/a/0", "1");

        PatchInstruction r9 = replace("/a/-", "1");
        PatchInstruction r10 = replace("/a/-", "1");
        PatchInstruction r11 = replace("/a/-", "1");
        PatchInstruction r12 = replace("/a/-", "1");

        PatchRequest request = new PatchRequest(Arrays.asList(replace("/a/c", "1"), replace("/a/c", 3)));

        assertThat(MAPPER.readValue(fixture("fixtures/patchrequest.json"), PatchRequest.class)).isEqualTo(request);
    }

    private PatchInstruction replace(String path, Object... values) {
        return new PatchInstruction(PatchOperation.REPLACE, path, new ArrayList<>(Arrays.asList(values)), null);
    }

    private PatchInstruction add(String path, Object... values) {
        return new PatchInstruction(PatchOperation.ADD, path, new ArrayList<>(Arrays.asList(values)), null);
    }

    private PatchInstruction test(String path, Object... values) {
        return new PatchInstruction(PatchOperation.TEST, path, new ArrayList<>(Arrays.asList(values)), null);
    }

    private PatchInstruction move(String path, String from) {
        return new PatchInstruction(PatchOperation.MOVE, path, new ArrayList<>(), from);
    }

    private PatchInstruction copy(String path, String from) {
        return new PatchInstruction(PatchOperation.COPY, path, new ArrayList<>(), from);
    }

    private PatchInstruction remove(String path) {
        return new PatchInstruction(PatchOperation.COPY, path, new ArrayList<>(), null);
    }
}
