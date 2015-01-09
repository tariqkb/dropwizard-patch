package io.progix.dropwizard.patch.test;

import com.sun.jersey.api.client.ClientResponse;
import io.dropwizard.testing.junit.ResourceTestRule;
import io.progix.dropwizard.patch.*;
import io.progix.dropwizard.patch.exception.InvalidPatchPathExceptionMapper;
import io.progix.dropwizard.patch.exception.PatchOperationNotSupportedExceptionMapper;
import io.progix.dropwizard.patch.exception.PatchTestFailedExceptionMapper;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

public class ExceptionMapperResourceTest {
    private UserStore dao = new UserStore();

    @Rule
    public ResourceTestRule resources = ResourceTestRule.builder().addProvider(PatchTestFailedExceptionMapper.class)
            .addProvider(PatchOperationNotSupportedExceptionMapper.class).addProvider(InvalidPatchPathExceptionMapper.class).
                    addResource(new UserResource(dao)).build();

    @Test
    public void testContextualInvalidPath() {
        Pet bird = new Pet(3, 4, Arrays.asList("Nick"));

        List<Object> pets = new ArrayList<Object>(Arrays.asList(bird));
        JsonPatchOperation addPetInstruction = new JsonPatchOperation(JsonPatchOperationType.ADD, "/incorrect/path", pets, "");

        ClientResponse response = resources.client().resource("/users/contextual/0").type(MediaType.APPLICATION_JSON)
                .method("PATCH", ClientResponse.class, Arrays.asList(addPetInstruction));

        assertThat(response.getStatus()).isEqualTo(422);

    }

    @Test
    public void testInvalidPath() {
        Pet bird = new Pet(3, 4, Arrays.asList("Nick"));

        List<Object> pets = new ArrayList<Object>(Arrays.asList(bird));
        JsonPatchOperation addPetInstruction = new JsonPatchOperation(JsonPatchOperationType.ADD, "/incorrect/path", pets, "");

        ClientResponse response = resources.client().resource("/users/0").type(MediaType.APPLICATION_JSON)
                .method("PATCH", ClientResponse.class, Arrays.asList(addPetInstruction));

        assertThat(response.getStatus()).isEqualTo(422);

    }

    @Test
    public void testContextualOperationNotSupported() {
        Pet bird = new Pet(3, 4, Arrays.asList("Nick"));

        List<Object> pets = new ArrayList<Object>(Arrays.asList(bird));
        JsonPatchOperation addPetInstruction = new JsonPatchOperation(JsonPatchOperationType.ADD, "/pets", pets, "");

        ClientResponse response = resources.client().resource("/users/contextual/no-operations/0").type(MediaType.APPLICATION_JSON)
                .method("PATCH", ClientResponse.class, Arrays.asList(addPetInstruction));

        assertThat(response.getStatus()).isEqualTo(415);

    }

    @Test
    public void testOperationNotSupported() {
        Pet bird = new Pet(3, 4, Arrays.asList("Nick"));

        List<Object> pets = new ArrayList<Object>(Arrays.asList(bird));
        JsonPatchOperation addPetInstruction = new JsonPatchOperation(JsonPatchOperationType.ADD, "/pets", pets, "");

        ClientResponse response = resources.client().resource("/users/no-operations/0").type(MediaType.APPLICATION_JSON)
                .method("PATCH", ClientResponse.class, Arrays.asList(addPetInstruction));

        assertThat(response.getStatus()).isEqualTo(415);

    }

    @Test
    public void testContextualTestFailure() {
        JsonPatchOperation testNameInstruction = new JsonPatchOperation(JsonPatchOperationType.TEST, "/name",
                new ArrayList<Object>(Arrays.asList("Allison")), "");

        ClientResponse response = resources.client().resource("/users/contextual/1").type(MediaType.APPLICATION_JSON)
                .method("PATCH", ClientResponse.class, Arrays.asList(testNameInstruction));

        assertThat(response.getStatus()).isEqualTo(412);

    }

    @Test
    public void testTestFailure() {
        JsonPatchOperation testNameInstruction = new JsonPatchOperation(JsonPatchOperationType.TEST, "/name",
                new ArrayList<Object>(Arrays.asList("Allison")), "");

        ClientResponse response = resources.client().resource("/users/1").type(MediaType.APPLICATION_JSON)
                .method("PATCH", ClientResponse.class, Arrays.asList(testNameInstruction));

        assertThat(response.getStatus()).isEqualTo(412);

    }
}
