package io.progix.dropwizard.patch.test;

import io.dropwizard.testing.junit.ResourceTestRule;
import io.progix.dropwizard.patch.Pet;
import io.progix.dropwizard.patch.User;
import io.progix.dropwizard.patch.UserResource;
import io.progix.dropwizard.patch.UserStore;
import io.progix.dropwizard.patch.hooks.PatchInstruction;
import io.progix.dropwizard.patch.hooks.PatchOperation;
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
    public ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new UserResource(dao))
            .build();

    @Test
    public void testManualAdd() {
        Pet bird = new Pet(3, 4, Arrays.asList("Nick"));

        List<Object> pets = new ArrayList<Object>(Arrays.asList(bird));
        PatchInstruction addInstruction = new PatchInstruction(PatchOperation.ADD, "/pets", pets, "");

        resources.client().resource("/users/0").type(MediaType.APPLICATION_JSON).method("PATCH", Arrays.asList(addInstruction));

        User user = new User(0, "Tariq", new ArrayList<>(Arrays.asList("tariq@progix.io")), new ArrayList<>(Arrays.asList(new Pet(0, 2, new ArrayList<>(Arrays.asList("Larry, Mogget"))), bird)));
        User oldUser = dao.getUsers().get(0);
        System.out.println("EQUAL?: "  + user.equals(oldUser));
        assertThat(dao.getUsers().get(0)).isEqualTo(user);

    }
}
