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

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.junit.ResourceTestRule;
import io.progix.dropwizard.patch.Pet;
import io.progix.dropwizard.patch.User;
import io.progix.dropwizard.patch.UserResource;
import io.progix.dropwizard.patch.UserStore;
import io.progix.dropwizard.patch.exception.PatchTestFailedExceptionMapper;
import io.progix.jackson.JsonPatchOperation;
import io.progix.jackson.JsonPatchOperationType;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class JsonPatchResourceTest {

    private String type;
    private UserStore dao = new UserStore();

    private ObjectMapper mapper = Jackson.newObjectMapper();

    @Rule
    public ResourceTestRule resources = ResourceTestRule.builder().addProvider(PatchTestFailedExceptionMapper.class).
            addResource(new UserResource(dao)).build();

    public JsonPatchResourceTest(String type) {
        this.type = type;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][]{{""}, {"default/"}, {"contextual/"}, {"default-with-custom/"}};
        return Arrays.asList(data);
    }

    @Test
    public void testAdd() {
        Pet bird = new Pet(3, 4, Arrays.asList("Nick"));

        JsonPatchOperation addPetInstruction = new JsonPatchOperation(JsonPatchOperationType.ADD,
                JsonPointer.compile("/pets/-"), mapper.convertValue(bird, JsonNode.class));

        resources.client().target("/users/" + type + "0").request(MediaType.APPLICATION_JSON)
                .method("PATCH", Entity.json(Arrays.asList(addPetInstruction)));

        User user = new User(0, "Tariq", new ArrayList<>(Arrays.asList("tariq@progix.io")),
                new ArrayList<>(Arrays.asList(new Pet(0, 2, new ArrayList<>(Arrays.asList("Larry, Mogget"))), bird)));
        assertThat(dao.getUsers().get(0)).isEqualTo(user);

    }

    @Test
    public void testCopy() {
        JsonPatchOperation copyPetInstruction = new JsonPatchOperation(JsonPatchOperationType.COPY,
                JsonPointer.compile("/pets/1"), JsonPointer.compile("/pets/0"));

        resources.client().target("/users/" + type + "0").request(MediaType.APPLICATION_JSON)
                .method("PATCH", Entity.json(Arrays.asList(copyPetInstruction)));

        User user = new User(0, "Tariq", new ArrayList<>(Arrays.asList("tariq@progix.io")), new ArrayList<>(
                Arrays.asList(new Pet(0, 2, new ArrayList<>(Arrays.asList("Larry, Mogget"))),
                        new Pet(0, 2, new ArrayList<>(Arrays.asList("Larry, Mogget"))))));
        assertThat(dao.getUsers().get(0)).isEqualTo(user);

    }

    @Test
    public void testMove() {
        JsonPatchOperation movePetInstruction = new JsonPatchOperation(JsonPatchOperationType.MOVE,
                JsonPointer.compile("/pets/1"), JsonPointer.compile("/pets/0"));

        resources.client().target("/users/" + type + "1").request(MediaType.APPLICATION_JSON)
                .method("PATCH", Entity.json(Arrays.asList(movePetInstruction)));

        User user = new User(1, "Alli", new ArrayList<>(Arrays.asList("alli@beeb.com")), new ArrayList<>(
                Arrays.asList(new Pet(1, 9, new ArrayList<>(Arrays.asList("Jonathan"))),
                        new Pet(0, 2, new ArrayList<>(Arrays.asList("Larry, Mogget"))))));
        assertThat(dao.getUsers().get(1)).isEqualTo(user);
    }

    @Test
    public void testRemove() {
        JsonPatchOperation removePetInstruction = new JsonPatchOperation(JsonPatchOperationType.REMOVE,
                JsonPointer.compile("/pets/0"));
        JsonPatchOperation removeEmailInstruction = new JsonPatchOperation(JsonPatchOperationType.REMOVE,
                JsonPointer.compile("/emailAddresses/0"));

        resources.client().target("/users/" + type + "0").request(MediaType.APPLICATION_JSON)
                .method("PATCH", Entity.json(Arrays.asList(removePetInstruction, removeEmailInstruction)));

        User user = new User(0, "Tariq", new ArrayList<String>(), new ArrayList<Pet>());
        assertThat(dao.getUsers().get(0)).isEqualTo(user);
    }

    @Test
    public void testReplace() {
        Pet bird = new Pet(3, 4, Arrays.asList("Nick"));
        Pet cat = new Pet(0, 2, new ArrayList<>(Arrays.asList("Larry, Mogget")));

        JsonPatchOperation replacePetInstruction = new JsonPatchOperation(JsonPatchOperationType.REPLACE,
                JsonPointer.compile("/pets/1"), mapper.convertValue(bird, JsonNode.class));
        JsonPatchOperation replaceNameInstruction = new JsonPatchOperation(JsonPatchOperationType.REPLACE,
                JsonPointer.compile("/name"), mapper.convertValue("Allison", JsonNode.class));
        JsonPatchOperation replaceEmailInstruction = new JsonPatchOperation(JsonPatchOperationType.REPLACE,
                JsonPointer.compile("/emailAddresses/0"), mapper.convertValue("allison@beeb.org", JsonNode.class));

        resources.client().target("/users/" + type + "1").request(MediaType.APPLICATION_JSON).method("PATCH",
                Entity.json(Arrays.asList(replacePetInstruction, replaceNameInstruction, replaceEmailInstruction)));

        User user = new User(1, "Allison", new ArrayList<>(Arrays.asList("allison@beeb.org")),
                new ArrayList<>(Arrays.asList(cat, bird)));
        assertThat(dao.getUsers().get(1)).isEqualTo(user);

    }

    @Test
    public void testTest() {
        Pet dog = new Pet(1, 9, new ArrayList<>(Arrays.asList("Jonathan")));

        JsonPatchOperation testPetInstruction = new JsonPatchOperation(JsonPatchOperationType.TEST,
                JsonPointer.compile("/pets/1"), mapper.convertValue(dog, JsonNode.class));
        JsonPatchOperation testNameInstruction = new JsonPatchOperation(JsonPatchOperationType.TEST,
                JsonPointer.compile("/name"), mapper.convertValue("Alli", JsonNode.class));
        JsonPatchOperation testEmailInstruction = new JsonPatchOperation(JsonPatchOperationType.TEST,
                JsonPointer.compile("/emailAddresses/0"), mapper.convertValue("alli@beeb.com", JsonNode.class));

        resources.client().target("/users/" + type + "1").request(MediaType.APPLICATION_JSON).method("PATCH",
                Entity.json(Arrays.asList(testPetInstruction, testNameInstruction, testEmailInstruction)));

    }

}