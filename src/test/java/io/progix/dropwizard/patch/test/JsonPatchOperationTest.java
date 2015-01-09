package io.progix.dropwizard.patch.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import io.progix.dropwizard.patch.*;
import io.progix.dropwizard.patch.exception.PatchOperationNotSupportedException;
import io.progix.dropwizard.patch.operations.*;
import io.progix.dropwizard.patch.operations.contextual.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class JsonPatchOperationTest {

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    ContextualJsonPatch<User> contextualJsonPatch;
    JsonPatch jsonPatch;

    @SuppressWarnings("unchecked")
    @Before
    public void init() throws IOException {
        contextualJsonPatch = (ContextualJsonPatch<User>) MAPPER.readValue(fixture("fixtures/patchrequest.json"), ContextualJsonPatch.class);
        jsonPatch = MAPPER.readValue(fixture("fixtures/patchrequest.json"), JsonPatch.class);
    }

    private Map<JsonPatchOperationType, Boolean> m(boolean includeAdd, boolean includeCopy, boolean includeMove, boolean includeRemove, boolean includeReplace, boolean includeTest) {
        Map<JsonPatchOperationType, Boolean> map = new HashMap<>();
        map.put(JsonPatchOperationType.ADD, includeAdd);
        map.put(JsonPatchOperationType.COPY, includeCopy);
        map.put(JsonPatchOperationType.MOVE, includeMove);
        map.put(JsonPatchOperationType.REMOVE, includeRemove);
        map.put(JsonPatchOperationType.REPLACE, includeReplace);
        map.put(JsonPatchOperationType.TEST, includeTest);
        return map;
    }

    private void testContextual(Map<JsonPatchOperationType, Boolean> includeMap) {
        final Map<JsonPatchOperationType, Boolean> didMap = m(false, false, false, false, false, false);

        if (includeMap.get(JsonPatchOperationType.ADD)) {
            contextualJsonPatch.setAdd(new ContextualAddOperation<User>() {
                @Override
                public void add(User context, JsonPath path, JsonPatchValue value) {
                    didMap.put(JsonPatchOperationType.ADD, true);
                }
            });
        }
        if (includeMap.get(JsonPatchOperationType.COPY)) {
            contextualJsonPatch.setCopy(new ContextualCopyOperation<User>() {
                @Override
                public void copy(User context, JsonPath from, JsonPath path) {
                    didMap.put(JsonPatchOperationType.COPY, true);
                }
            });
        }
        if (includeMap.get(JsonPatchOperationType.MOVE)) {
            contextualJsonPatch.setMove(new ContextualMoveOperation<User>() {
                @Override
                public void move(User context, JsonPath from, JsonPath path) {
                    didMap.put(JsonPatchOperationType.MOVE, true);
                }
            });
        }
        if (includeMap.get(JsonPatchOperationType.REMOVE)) {
            contextualJsonPatch.setRemove(new ContextualRemoveOperation<User>() {
                @Override
                public void remove(User context, JsonPath path) {
                    didMap.put(JsonPatchOperationType.REMOVE, true);
                }
            });
        }
        if (includeMap.get(JsonPatchOperationType.REPLACE)) {
            contextualJsonPatch.setReplace(new ContextualReplaceOperation<User>() {
                @Override
                public void replace(User context, JsonPath path, JsonPatchValue value) {
                    didMap.put(JsonPatchOperationType.REPLACE, true);
                }
            });
        }
        if (includeMap.get(JsonPatchOperationType.TEST)) {
            contextualJsonPatch.setTest(new ContextualTestOperation<User>() {
                @Override
                public boolean test(User context, JsonPath path, JsonPatchValue value) {
                    didMap.put(JsonPatchOperationType.TEST, true);
                    return true;
                }
            });
        }

        try {
            contextualJsonPatch.apply(new User());
        } catch (PatchOperationNotSupportedException e) {
            for (Map.Entry<JsonPatchOperationType, Boolean> entry : includeMap.entrySet()) {
                JsonPatchOperationType type = entry.getKey();
                boolean included = entry.getValue();

                if (e.getOperations().contains(type) && included) {
                    fail("PatchOperationNotSupported when should have been supported (" + type + ")");
                }
            }
        }

        assertEquals(includeMap, didMap);
    }

    private void test(Map<JsonPatchOperationType, Boolean> includeMap) {
        final Map<JsonPatchOperationType, Boolean> didMap = m(false, false, false, false, false, false);

        if (includeMap.get(JsonPatchOperationType.ADD)) {
            jsonPatch.setAdd(new AddOperation() {
                @Override
                public void add(JsonPath path, JsonPatchValue value) {
                    didMap.put(JsonPatchOperationType.ADD, true);
                }
            });
        }
        if (includeMap.get(JsonPatchOperationType.COPY)) {
            jsonPatch.setCopy(new CopyOperation() {
                @Override
                public void copy(JsonPath from, JsonPath path) {
                    didMap.put(JsonPatchOperationType.COPY, true);
                }
            });
        }
        if (includeMap.get(JsonPatchOperationType.MOVE)) {
            jsonPatch.setMove(new MoveOperation() {
                @Override
                public void move(JsonPath from, JsonPath path) {
                    didMap.put(JsonPatchOperationType.MOVE, true);
                }
            });
        }
        if (includeMap.get(JsonPatchOperationType.REMOVE)) {
            jsonPatch.setRemove(new RemoveOperation() {
                @Override
                public void remove(JsonPath path) {
                    didMap.put(JsonPatchOperationType.REMOVE, true);
                }
            });
        }
        if (includeMap.get(JsonPatchOperationType.REPLACE)) {
            jsonPatch.setReplace(new ReplaceOperation() {
                @Override
                public void replace(JsonPath path, JsonPatchValue value) {
                    didMap.put(JsonPatchOperationType.REPLACE, true);
                }
            });
        }
        if (includeMap.get(JsonPatchOperationType.TEST)) {
            jsonPatch.setTest(new TestOperation() {
                @Override
                public boolean test(JsonPath path, JsonPatchValue value) {
                    didMap.put(JsonPatchOperationType.TEST, true);
                    return true;
                }
            });
        }

        try {
            jsonPatch.apply();
        } catch (PatchOperationNotSupportedException e) {
            for (Map.Entry<JsonPatchOperationType, Boolean> entry : includeMap.entrySet()) {
                JsonPatchOperationType type = entry.getKey();
                boolean included = entry.getValue();

                if (e.getOperations().contains(type) && included) {
                    fail("PatchOperationNotSupported when should have been supported (" + type + ")");
                }
            }
        }
        assertEquals(includeMap, didMap);
    }

    @Test
    public void testJsonPatch() {

        test(m(true, true, true, true, true, true));
        test(m(true, true, true, true, true, false));
        test(m(true, true, true, true, false, true));
        test(m(true, true, true, true, false, false));
        test(m(true, true, true, false, true, true));
        test(m(true, true, true, false, true, false));
        test(m(true, true, true, false, false, true));
        test(m(true, true, true, false, false, false));
        test(m(true, true, false, true, true, true));
        test(m(true, true, false, true, true, false));
        test(m(true, true, false, true, false, true));
        test(m(true, true, false, true, false, false));
        test(m(true, true, false, false, true, true));
        test(m(true, true, false, false, true, false));
        test(m(true, true, false, false, false, true));
        test(m(true, true, false, false, false, false));

        test(m(true, false, true, true, true, true));
        test(m(true, false, true, true, true, false));
        test(m(true, false, true, true, false, true));
        test(m(true, false, true, true, false, false));
        test(m(true, false, true, false, true, true));
        test(m(true, false, true, false, true, false));
        test(m(true, false, true, false, false, true));
        test(m(true, false, true, false, false, false));
        test(m(true, false, false, true, true, true));
        test(m(true, false, false, true, true, false));
        test(m(true, false, false, true, false, true));
        test(m(true, false, false, true, false, false));
        test(m(true, false, false, false, true, true));
        test(m(true, false, false, false, true, false));
        test(m(true, false, false, false, false, true));
        test(m(true, false, false, false, false, false));

        test(m(false, true, true, true, true, true));
        test(m(false, true, true, true, true, false));
        test(m(false, true, true, true, false, true));
        test(m(false, true, true, true, false, false));
        test(m(false, true, true, false, true, true));
        test(m(false, true, true, false, true, false));
        test(m(false, true, true, false, false, true));
        test(m(false, true, true, false, false, false));
        test(m(false, true, false, true, true, true));
        test(m(false, true, false, true, true, false));
        test(m(false, true, false, true, false, true));
        test(m(false, true, false, true, false, false));
        test(m(false, true, false, false, true, true));
        test(m(false, true, false, false, true, false));
        test(m(false, true, false, false, false, true));
        test(m(false, true, false, false, false, false));

        test(m(false, false, true, true, true, true));
        test(m(false, false, true, true, true, false));
        test(m(false, false, true, true, false, true));
        test(m(false, false, true, true, false, false));
        test(m(false, false, true, false, true, true));
        test(m(false, false, true, false, true, false));
        test(m(false, false, true, false, false, true));
        test(m(false, false, true, false, false, false));
        test(m(false, false, false, true, true, true));
        test(m(false, false, false, true, true, false));
        test(m(false, false, false, true, false, true));
        test(m(false, false, false, true, false, false));
        test(m(false, false, false, false, true, true));
        test(m(false, false, false, false, true, false));
        test(m(false, false, false, false, false, true));
        test(m(false, false, false, false, false, false));
    }

    @Test
    public void testContextualJsonPatch() {

        testContextual(m(true, true, true, true, true, true));
        testContextual(m(true, true, true, true, true, false));
        testContextual(m(true, true, true, true, false, true));
        testContextual(m(true, true, true, true, false, false));
        testContextual(m(true, true, true, false, true, true));
        testContextual(m(true, true, true, false, true, false));
        testContextual(m(true, true, true, false, false, true));
        testContextual(m(true, true, true, false, false, false));
        testContextual(m(true, true, false, true, true, true));
        testContextual(m(true, true, false, true, true, false));
        testContextual(m(true, true, false, true, false, true));
        testContextual(m(true, true, false, true, false, false));
        testContextual(m(true, true, false, false, true, true));
        testContextual(m(true, true, false, false, true, false));
        testContextual(m(true, true, false, false, false, true));
        testContextual(m(true, true, false, false, false, false));

        testContextual(m(true, false, true, true, true, true));
        testContextual(m(true, false, true, true, true, false));
        testContextual(m(true, false, true, true, false, true));
        testContextual(m(true, false, true, true, false, false));
        testContextual(m(true, false, true, false, true, true));
        testContextual(m(true, false, true, false, true, false));
        testContextual(m(true, false, true, false, false, true));
        testContextual(m(true, false, true, false, false, false));
        testContextual(m(true, false, false, true, true, true));
        testContextual(m(true, false, false, true, true, false));
        testContextual(m(true, false, false, true, false, true));
        testContextual(m(true, false, false, true, false, false));
        testContextual(m(true, false, false, false, true, true));
        testContextual(m(true, false, false, false, true, false));
        testContextual(m(true, false, false, false, false, true));
        testContextual(m(true, false, false, false, false, false));

        testContextual(m(false, true, true, true, true, true));
        testContextual(m(false, true, true, true, true, false));
        testContextual(m(false, true, true, true, false, true));
        testContextual(m(false, true, true, true, false, false));
        testContextual(m(false, true, true, false, true, true));
        testContextual(m(false, true, true, false, true, false));
        testContextual(m(false, true, true, false, false, true));
        testContextual(m(false, true, true, false, false, false));
        testContextual(m(false, true, false, true, true, true));
        testContextual(m(false, true, false, true, true, false));
        testContextual(m(false, true, false, true, false, true));
        testContextual(m(false, true, false, true, false, false));
        testContextual(m(false, true, false, false, true, true));
        testContextual(m(false, true, false, false, true, false));
        testContextual(m(false, true, false, false, false, true));
        testContextual(m(false, true, false, false, false, false));

        testContextual(m(false, false, true, true, true, true));
        testContextual(m(false, false, true, true, true, false));
        testContextual(m(false, false, true, true, false, true));
        testContextual(m(false, false, true, true, false, false));
        testContextual(m(false, false, true, false, true, true));
        testContextual(m(false, false, true, false, true, false));
        testContextual(m(false, false, true, false, false, true));
        testContextual(m(false, false, true, false, false, false));
        testContextual(m(false, false, false, true, true, true));
        testContextual(m(false, false, false, true, true, false));
        testContextual(m(false, false, false, true, false, true));
        testContextual(m(false, false, false, true, false, false));
        testContextual(m(false, false, false, false, true, true));
        testContextual(m(false, false, false, false, true, false));
        testContextual(m(false, false, false, false, false, true));
        testContextual(m(false, false, false, false, false, false));
    }
}
