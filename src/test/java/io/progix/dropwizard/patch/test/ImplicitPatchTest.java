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
import io.progix.dropwizard.patch.explicit.PatchInstruction;
import io.progix.dropwizard.patch.explicit.PatchOperation;
import io.progix.dropwizard.patch.implicit.PatchedProvider;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

public class ImplicitPatchTest {

    private UserStore dao = new UserStore();

    @Rule
    public ResourceTestRule resources = ResourceTestRule.builder().addProvider(PatchTestFailedExceptionMapper.class).
            addResource(new UserResource(dao)).addProvider(PatchedProvider.class).build();

    @Test
    public void test() {
        Pet bird = new Pet(3, 4, Arrays.asList("Nick"));

        List<Object> pets = new ArrayList<Object>(Arrays.asList(bird));
        PatchInstruction addPetInstruction = new PatchInstruction(PatchOperation.ADD, "/pets", pets, "");

        User updated = resources.client().resource("/users/implicit/0").type(MediaType.APPLICATION_JSON)
                .method("PATCH", User.class, Arrays.asList(addPetInstruction));

        User user = new User(0, "Tariq", new ArrayList<>(Arrays.asList("tariq@progix.io")),
                new ArrayList<>(Arrays.asList(new Pet(0, 2, new ArrayList<>(Arrays.asList("Larry, Mogget"))), bird)));

        assertThat(updated).isEqualTo(user);
    }
}
