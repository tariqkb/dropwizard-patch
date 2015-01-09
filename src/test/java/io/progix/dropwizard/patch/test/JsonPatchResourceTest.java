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

import io.dropwizard.testing.junit.ResourceTestRule;
import io.progix.dropwizard.patch.*;
import io.progix.dropwizard.patch.exception.PatchTestFailedExceptionMapper;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class JsonPatchResourceTest {

    private String type;
    private UserStore dao = new UserStore();

    @Rule
    public ResourceTestRule resources = ResourceTestRule.builder().addProvider(PatchTestFailedExceptionMapper.class).
            addResource(new UserResource(dao)).build();

    public JsonPatchResourceTest(String type) {
        this.type = type;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][]{{""}, {"contextual/"}};
        return Arrays.asList(data);
    }

    @Test
    public void testAdd() {
        Pet bird = new Pet(3, 4, Arrays.asList("Nick"));

        List<Object> pets = new ArrayList<Object>(Arrays.asList(bird));
        JsonPatchOperation addPetInstruction = new JsonPatchOperation(JsonPatchOperationType.ADD, "/pets", pets, "");

        resources.client().resource("/users/" + type + "0").type(MediaType.APPLICATION_JSON)
                .method("PATCH", Arrays.asList(addPetInstruction));

        User user = new User(0, "Tariq", new ArrayList<>(Arrays.asList("tariq@progix.io")),
                new ArrayList<>(Arrays.asList(new Pet(0, 2, new ArrayList<>(Arrays.asList("Larry, Mogget"))), bird)));
        assertThat(dao.getUsers().get(0)).isEqualTo(user);

    }

    @Test
    public void testCopy() {
        JsonPatchOperation copyPetInstruction = new JsonPatchOperation(JsonPatchOperationType.COPY, "/pets/1", null, "/pets/0");

        resources.client().resource("/users/" + type + "0").type(MediaType.APPLICATION_JSON)
                .method("PATCH", Arrays.asList(copyPetInstruction));

        User user = new User(0, "Tariq", new ArrayList<>(Arrays.asList("tariq@progix.io")), new ArrayList<>(
                Arrays.asList(new Pet(0, 2, new ArrayList<>(Arrays.asList("Larry, Mogget"))),
                        new Pet(0, 2, new ArrayList<>(Arrays.asList("Larry, Mogget"))))));
        assertThat(dao.getUsers().get(0)).isEqualTo(user);

    }

    @Test
    public void testMove() {
        JsonPatchOperation movePetInstruction = new JsonPatchOperation(JsonPatchOperationType.MOVE, "/pets/1", null, "/pets/0");

        resources.client().resource("/users/" + type + "1").type(MediaType.APPLICATION_JSON)
                .method("PATCH", Arrays.asList(movePetInstruction));

        User user = new User(1, "Alli", new ArrayList<>(Arrays.asList("alli@beeb.com")), new ArrayList<>(
                Arrays.asList(new Pet(1, 9, new ArrayList<>(Arrays.asList("Jonathan"))),
                        new Pet(0, 2, new ArrayList<>(Arrays.asList("Larry, Mogget"))))));
        assertThat(dao.getUsers().get(1)).isEqualTo(user);
    }

    @Test
    public void testRemove() {
        JsonPatchOperation removePetInstruction = new JsonPatchOperation(JsonPatchOperationType.REMOVE, "/pets/0", null, "");
        JsonPatchOperation removeEmailInstruction = new JsonPatchOperation(JsonPatchOperationType.REMOVE, "/emailAddresses/0", null,
                "");

        resources.client().resource("/users/" + type + "0").type(MediaType.APPLICATION_JSON)
                .method("PATCH", Arrays.asList(removePetInstruction, removeEmailInstruction));

        User user = new User(0, "Tariq", new ArrayList<String>(), new ArrayList<Pet>());
        assertThat(dao.getUsers().get(0)).isEqualTo(user);
    }

    @Test
    public void testReplace() {
        Pet bird = new Pet(3, 4, Arrays.asList("Nick"));
        Pet cat = new Pet(0, 2, new ArrayList<>(Arrays.asList("Larry, Mogget")));

        JsonPatchOperation replacePetInstruction = new JsonPatchOperation(JsonPatchOperationType.REPLACE, "/pets/1",
                new ArrayList<Object>(Arrays.asList(bird)), "");
        JsonPatchOperation replaceNameInstruction = new JsonPatchOperation(JsonPatchOperationType.REPLACE, "/name",
                new ArrayList<Object>(Arrays.asList("Allison")), "");
        JsonPatchOperation replaceEmailInstruction = new JsonPatchOperation(JsonPatchOperationType.REPLACE, "/emailAddresses/0",
                new ArrayList<Object>(Arrays.asList("allison@beeb.org")), "");

        resources.client().resource("/users/" + type + "1").type(MediaType.APPLICATION_JSON)
                .method("PATCH", Arrays.asList(replacePetInstruction, replaceNameInstruction, replaceEmailInstruction));

        User user = new User(1, "Allison", new ArrayList<>(Arrays.asList("allison@beeb.org")),
                new ArrayList<>(Arrays.asList(cat, bird)));
        assertThat(dao.getUsers().get(1)).isEqualTo(user);

    }

    @Test
    public void testTest() {
        Pet dog = new Pet(1, 9, new ArrayList<>(Arrays.asList("Jonathan")));

        JsonPatchOperation testPetInstruction = new JsonPatchOperation(JsonPatchOperationType.TEST, "/pets/1",
                new ArrayList<Object>(Arrays.asList(dog)), "");
        JsonPatchOperation testNameInstruction = new JsonPatchOperation(JsonPatchOperationType.TEST, "/name",
                new ArrayList<Object>(Arrays.asList("Alli")), "");
        JsonPatchOperation testEmailInstruction = new JsonPatchOperation(JsonPatchOperationType.TEST, "/emailAddresses/0",
                new ArrayList<Object>(Arrays.asList("alli@beeb.com")), "");

        resources.client().resource("/users/" + type + "1").type(MediaType.APPLICATION_JSON)
                .method("PATCH", Arrays.asList(testPetInstruction, testNameInstruction, testEmailInstruction));

    }

}