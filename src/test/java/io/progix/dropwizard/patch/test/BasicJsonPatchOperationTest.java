package io.progix.dropwizard.patch.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import io.progix.dropwizard.patch.*;
import io.progix.dropwizard.patch.exception.PatchOperationNotSupportedException;
import io.progix.dropwizard.patch.operations.*;
import io.progix.dropwizard.patch.operations.contextual.*;
import io.progix.jackson.JsonPatchOperationType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.*;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(Parameterized.class)
public class BasicJsonPatchOperationTest {

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    private final Map<JsonPatchOperationType, Boolean> includeMap;

    public BasicJsonPatchOperationTest(Map<JsonPatchOperationType, Boolean> includeMap) {
        this.includeMap = includeMap;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][]{{m(true, true, true, true, true, true)},
                {m(true, true, true, true, true, false)},
                {m(true, true, true, true, false, true)},
                {m(true, true, true, true, false, false)},
                {m(true, true, true, false, true, true)},
                {m(true, true, true, false, true, false)},
                {m(true, true, true, false, false, true)},
                {m(true, true, true, false, false, false)},
                {m(true, true, false, true, true, true)},
                {m(true, true, false, true, true, false)},
                {m(true, true, false, true, false, true)},
                {m(true, true, false, true, false, false)},
                {m(true, true, false, false, true, true)},
                {m(true, true, false, false, true, false)},
                {m(true, true, false, false, false, true)},
                {m(true, true, false, false, false, false)},

                {m(true, false, true, true, true, true)},
                {m(true, false, true, true, true, false)},
                {m(true, false, true, true, false, true)},
                {m(true, false, true, true, false, false)},
                {m(true, false, true, false, true, true)},
                {m(true, false, true, false, true, false)},
                {m(true, false, true, false, false, true)},
                {m(true, false, true, false, false, false)},
                {m(true, false, false, true, true, true)},
                {m(true, false, false, true, true, false)},
                {m(true, false, false, true, false, true)},
                {m(true, false, false, true, false, false)},
                {m(true, false, false, false, true, true)},
                {m(true, false, false, false, true, false)},
                {m(true, false, false, false, false, true)},
                {m(true, false, false, false, false, false)},

                {m(false, true, true, true, true, true)},
                {m(false, true, true, true, true, false)},
                {m(false, true, true, true, false, true)},
                {m(false, true, true, true, false, false)},
                {m(false, true, true, false, true, true)},
                {m(false, true, true, false, true, false)},
                {m(false, true, true, false, false, true)},
                {m(false, true, true, false, false, false)},
                {m(false, true, false, true, true, true)},
                {m(false, true, false, true, true, false)},
                {m(false, true, false, true, false, true)},
                {m(false, true, false, true, false, false)},
                {m(false, true, false, false, true, true)},
                {m(false, true, false, false, true, false)},
                {m(false, true, false, false, false, true)},
                {m(false, true, false, false, false, false)},

                {m(false, false, true, true, true, true)},
                {m(false, false, true, true, true, false)},
                {m(false, false, true, true, false, true)},
                {m(false, false, true, true, false, false)},
                {m(false, false, true, false, true, true)},
                {m(false, false, true, false, true, false)},
                {m(false, false, true, false, false, true)},
                {m(false, false, true, false, false, false)},
                {m(false, false, false, true, true, true)},
                {m(false, false, false, true, true, false)},
                {m(false, false, false, true, false, true)},
                {m(false, false, false, true, false, false)},
                {m(false, false, false, false, true, true)},
                {m(false, false, false, false, true, false)},
                {m(false, false, false, false, false, true)},
                {m(false, false, false, false, false, false)}};
        return Arrays.asList(data);
    }

    private static Map<JsonPatchOperationType, Boolean> m(boolean includeAdd, boolean includeCopy, boolean includeMove, boolean includeRemove, boolean includeReplace, boolean includeTest) {
        Map<JsonPatchOperationType, Boolean> map = new HashMap<>();
        map.put(JsonPatchOperationType.ADD, includeAdd);
        map.put(JsonPatchOperationType.COPY, includeCopy);
        map.put(JsonPatchOperationType.MOVE, includeMove);
        map.put(JsonPatchOperationType.REMOVE, includeRemove);
        map.put(JsonPatchOperationType.REPLACE, includeReplace);
        map.put(JsonPatchOperationType.TEST, includeTest);
        return map;
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testContextual() throws IOException {
        ContextualJsonPatch<User> contextualJsonPatch = (ContextualJsonPatch<User>) MAPPER.readValue(fixture("fixtures/patchrequest.json"), ContextualJsonPatch.class);

        final Map<JsonPatchOperationType, Boolean> didMap = m(false, false, false, false, false, false);

        if (includeMap.get(JsonPatchOperationType.ADD)) {
            contextualJsonPatch.setAdd(new ContextualAddOperation<User>() {
                @Override
                public User add(User context, JsonPath path, JsonPatchValue value) {
                    didMap.put(JsonPatchOperationType.ADD, true);
                    return context;
                }
            });
        }
        if (includeMap.get(JsonPatchOperationType.COPY)) {
            contextualJsonPatch.setCopy(new ContextualCopyOperation<User>() {
                @Override
                public User copy(User context, JsonPath from, JsonPath path) {
                    didMap.put(JsonPatchOperationType.COPY, true);
                    return context;
                }
            });
        }
        if (includeMap.get(JsonPatchOperationType.MOVE)) {
            contextualJsonPatch.setMove(new ContextualMoveOperation<User>() {
                @Override
                public User move(User context, JsonPath from, JsonPath path) {
                    didMap.put(JsonPatchOperationType.MOVE, true);
                    return context;
                }
            });
        }
        if (includeMap.get(JsonPatchOperationType.REMOVE)) {
            contextualJsonPatch.setRemove(new ContextualRemoveOperation<User>() {
                @Override
                public User remove(User context, JsonPath path) {
                    didMap.put(JsonPatchOperationType.REMOVE, true);
                    return context;
                }
            });
        }
        if (includeMap.get(JsonPatchOperationType.REPLACE)) {
            contextualJsonPatch.setReplace(new ContextualReplaceOperation<User>() {
                @Override
                public User replace(User context, JsonPath path, JsonPatchValue value) {
                    didMap.put(JsonPatchOperationType.REPLACE, true);
                    return context;
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

        Set<JsonPatchOperationType> operationsInCaughtException = new HashSet<>();
        try {
            contextualJsonPatch.apply(new User());
        } catch (PatchOperationNotSupportedException e) {
            operationsInCaughtException.addAll(e.getOperations());
        }

        assertEquals(includeMap, didMap);

        for (Map.Entry<JsonPatchOperationType, Boolean> entry : includeMap.entrySet()) {
            JsonPatchOperationType type = entry.getKey();
            boolean included = entry.getValue();

            if (operationsInCaughtException.contains(type) && included) {
                fail("PatchOperationNotSupported exception thrown when should have been supported (" + type + ")");
            } else if (!operationsInCaughtException.contains(type) && !included) {
                fail("No PatchOperationNotSupported exception thrown when it should have been (" + type + ")");
            }
        }

    }

    @Test
    public void test() throws IOException {
        BasicJsonPatch basicJsonPatch = MAPPER.readValue(fixture("fixtures/patchrequest.json"), BasicJsonPatch.class);

        final Map<JsonPatchOperationType, Boolean> didMap = m(false, false, false, false, false, false);

        if (includeMap.get(JsonPatchOperationType.ADD)) {
            basicJsonPatch.setAdd(new AddOperation() {
                @Override
                public void add(JsonPath path, JsonPatchValue value) {
                    didMap.put(JsonPatchOperationType.ADD, true);
                }
            });
        }
        if (includeMap.get(JsonPatchOperationType.COPY)) {
            basicJsonPatch.setCopy(new CopyOperation() {
                @Override
                public void copy(JsonPath from, JsonPath path) {
                    didMap.put(JsonPatchOperationType.COPY, true);
                }
            });
        }
        if (includeMap.get(JsonPatchOperationType.MOVE)) {
            basicJsonPatch.setMove(new MoveOperation() {
                @Override
                public void move(JsonPath from, JsonPath path) {
                    didMap.put(JsonPatchOperationType.MOVE, true);
                }
            });
        }
        if (includeMap.get(JsonPatchOperationType.REMOVE)) {
            basicJsonPatch.setRemove(new RemoveOperation() {
                @Override
                public void remove(JsonPath path) {
                    didMap.put(JsonPatchOperationType.REMOVE, true);
                }
            });
        }
        if (includeMap.get(JsonPatchOperationType.REPLACE)) {
            basicJsonPatch.setReplace(new ReplaceOperation() {
                @Override
                public void replace(JsonPath path, JsonPatchValue value) {
                    didMap.put(JsonPatchOperationType.REPLACE, true);
                }
            });
        }
        if (includeMap.get(JsonPatchOperationType.TEST)) {
            basicJsonPatch.setTest(new TestOperation() {
                @Override
                public boolean test(JsonPath path, JsonPatchValue value) {
                    didMap.put(JsonPatchOperationType.TEST, true);
                    return true;
                }
            });
        }

        Set<JsonPatchOperationType> operationsInCaughtException = new HashSet<>();
        try {
            basicJsonPatch.apply();
        } catch (PatchOperationNotSupportedException e) {
            operationsInCaughtException.addAll(e.getOperations());
        }

        assertEquals(includeMap, didMap);

        for (Map.Entry<JsonPatchOperationType, Boolean> entry : includeMap.entrySet()) {
            JsonPatchOperationType type = entry.getKey();
            boolean included = entry.getValue();

            if (operationsInCaughtException.contains(type) && included) {
                fail("PatchOperationNotSupported exception thrown when should have been supported (" + type + ")");
            } else if (!operationsInCaughtException.contains(type) && !included) {
                fail("No PatchOperationNotSupported exception thrown when it should have been (" + type + ")");
            }
        }
    }

}
