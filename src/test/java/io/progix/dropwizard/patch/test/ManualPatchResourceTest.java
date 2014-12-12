package io.progix.dropwizard.patch.test;

import com.sun.jersey.api.client.ClientResponse;
import io.dropwizard.testing.junit.ResourceTestRule;
import io.progix.dropwizard.patch.*;
import io.progix.dropwizard.patch.explicit.PatchInstruction;
import io.progix.dropwizard.patch.explicit.PatchOperation;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

public class ManualPatchResourceTest {

    private UserStore dao = new UserStore();

    @Rule
    public ResourceTestRule resources = ResourceTestRule.builder().addProvider(PatchTestFailedExceptionMapper.class).
            addResource(new UserResource(dao)).build();

    @Test
    public void testManualAdd() {
        Pet bird = new Pet(3, 4, Arrays.asList("Nick"));

        List<Object> pets = new ArrayList<Object>(Arrays.asList(bird));
        PatchInstruction addPetInstruction = new PatchInstruction(PatchOperation.ADD, "/pets", pets, "");

        resources.client().resource("/users/0").type(MediaType.APPLICATION_JSON)
                .method("PATCH", Arrays.asList(addPetInstruction));

        User user = new User(0, "Tariq", new ArrayList<>(Arrays.asList("tariq@progix.io")),
                new ArrayList<>(Arrays.asList(new Pet(0, 2, new ArrayList<>(Arrays.asList("Larry, Mogget"))), bird)));
        assertThat(dao.getUsers().get(0)).isEqualTo(user);

    }

    @Test
    public void testManualCopy() {
        PatchInstruction copyPetInstruction = new PatchInstruction(PatchOperation.COPY, "/pets/1", null, "/pets/0");

        resources.client().resource("/users/0").type(MediaType.APPLICATION_JSON)
                .method("PATCH", Arrays.asList(copyPetInstruction));

        User user = new User(0, "Tariq", new ArrayList<>(Arrays.asList("tariq@progix.io")), new ArrayList<>(
                Arrays.asList(new Pet(0, 2, new ArrayList<>(Arrays.asList("Larry, Mogget"))),
                        new Pet(0, 2, new ArrayList<>(Arrays.asList("Larry, Mogget"))))));
        assertThat(dao.getUsers().get(0)).isEqualTo(user);

    }

    @Test
    public void testManualMove() {
        PatchInstruction movePetInstruction = new PatchInstruction(PatchOperation.MOVE, "/pets/1", null, "/pets/0");

        resources.client().resource("/users/1").type(MediaType.APPLICATION_JSON)
                .method("PATCH", Arrays.asList(movePetInstruction));

        User user = new User(1, "Alli", new ArrayList<>(Arrays.asList("alli@beeb.com")), new ArrayList<>(
                Arrays.asList(new Pet(1, 9, new ArrayList<>(Arrays.asList("Jonathan"))),
                        new Pet(0, 2, new ArrayList<>(Arrays.asList("Larry, Mogget"))))));
        assertThat(dao.getUsers().get(1)).isEqualTo(user);
    }

    @Test
    public void testManualRemove() {
        PatchInstruction removePetInstruction = new PatchInstruction(PatchOperation.REMOVE, "/pets/0", null, "");
        PatchInstruction removeEmailInstruction = new PatchInstruction(PatchOperation.REMOVE, "/emailAddresses/0", null,
                "");

        resources.client().resource("/users/0").type(MediaType.APPLICATION_JSON)
                .method("PATCH", Arrays.asList(removePetInstruction, removeEmailInstruction));

        User user = new User(0, "Tariq", new ArrayList<String>(), new ArrayList<Pet>());
        assertThat(dao.getUsers().get(0)).isEqualTo(user);
    }

    @Test
    public void testManualReplace() {
        Pet bird = new Pet(3, 4, Arrays.asList("Nick"));
        Pet cat = new Pet(0, 2, new ArrayList<>(Arrays.asList("Larry, Mogget")));

        PatchInstruction replacePetInstruction = new PatchInstruction(PatchOperation.REPLACE, "/pets/1",
                new ArrayList<Object>(Arrays.asList(bird)), "");
        PatchInstruction replaceNameInstruction = new PatchInstruction(PatchOperation.REPLACE, "/name",
                new ArrayList<Object>(Arrays.asList("Allison")), "");
        PatchInstruction replaceEmailInstruction = new PatchInstruction(PatchOperation.REPLACE, "/emailAddresses/0",
                new ArrayList<Object>(Arrays.asList("allison@beeb.org")), "");

        resources.client().resource("/users/1").type(MediaType.APPLICATION_JSON)
                .method("PATCH", Arrays.asList(replacePetInstruction, replaceNameInstruction, replaceEmailInstruction));

        User user = new User(1, "Allison", new ArrayList<>(Arrays.asList("allison@beeb.org")),
                new ArrayList<>(Arrays.asList(cat, bird)));
        assertThat(dao.getUsers().get(1)).isEqualTo(user);

    }

    @Test
    public void testManualTest() {
        Pet dog = new Pet(1, 9, new ArrayList<>(Arrays.asList("Jonathan")));

        PatchInstruction testPetInstruction = new PatchInstruction(PatchOperation.TEST, "/pets/1",
                new ArrayList<Object>(Arrays.asList(dog)), "");
        PatchInstruction testNameInstruction = new PatchInstruction(PatchOperation.TEST, "/name",
                new ArrayList<Object>(Arrays.asList("Alli")), "");
        PatchInstruction testEmailInstruction = new PatchInstruction(PatchOperation.TEST, "/emailAddresses/0",
                new ArrayList<Object>(Arrays.asList("alli@beeb.com")), "");

        resources.client().resource("/users/1").type(MediaType.APPLICATION_JSON)
                .method("PATCH", Arrays.asList(testPetInstruction, testNameInstruction, testEmailInstruction));

    }

    @Test
    public void testManualTestFailure() {
        PatchInstruction testNameInstruction = new PatchInstruction(PatchOperation.TEST, "/name",
                new ArrayList<Object>(Arrays.asList("Allison")), "");

        ClientResponse response = resources.client().resource("/users/1").type(MediaType.APPLICATION_JSON)
                .method("PATCH", ClientResponse.class, Arrays.asList(testNameInstruction));

        assertThat(response.getStatus()).isEqualTo(412);

    }
}