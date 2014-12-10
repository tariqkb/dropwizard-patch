package io.progix.dropwizard.patch;

import io.dropwizard.jersey.PATCH;
import io.progix.dropwizard.patch.explicit.JsonPatchValue;
import io.progix.dropwizard.patch.explicit.JsonPath;
import io.progix.dropwizard.patch.explicit.PatchRequest;
import io.progix.dropwizard.patch.explicit.handlers.*;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    private static final Logger logger = Logger.getLogger(UserResource.class);

    private List<User> users = new ArrayList<User>();

    public UserResource(UserStore store) {
        this.users = store.getUsers();
    }

    @GET
    public List<User> getUsers() {
        return users;
    }

    @GET
    @Path("/{id}")
    public User getUser(@PathParam("id") long userId) {
        for (User user : users) {
            if (user.getId() == userId) {
                return user;
            }
        }
        throw new RuntimeException("No user found");
    }

    @PATCH
    @Path("/{id}")
    public void updateUser(@PathParam("id") int id, PatchRequest request) {

        final User user = users.get(id);

        request.add(new AddHandler() {

            @Override
            public boolean add(JsonPath path, JsonPatchValue value) {
                if (path.property(0).exists()) {
                    if (path.property(0).is("pets")) {
                        if (path.element(1).exists()) {
                            int petIndex = path.element(1).val();
                            if (!path.property(2).exists()) {
                                user.getPets().addAll(petIndex, value.to(Pet.class));
                                return true;
                            }
                        } else {
                            user.getPets().addAll(value.to(Pet.class));
                            return true;
                        }
                    } else if(path.property(0).is("name")) {
                        user.setName(value.one(String.class));
                        return true;
                    } else if(path.property(0).is("emailAddresses")) {
                        if(path.element(1).exists()) {
                            user.getEmailAddresses().addAll(path.element(1).val(), value.to(String.class));
                        } else {
                            user.getEmailAddresses().addAll(value.to(String.class));
                        }
                        return true;
                    }
                }
                return false;
            }
        });

        request.copy(new CopyHandler() {
            @Override
            public boolean copy(JsonPath from, JsonPath path) {

                return false;
            }
        });

        request.move(new MoveHandler() {
            @Override
            public boolean move(JsonPath from, JsonPath path) {
                return false;
            }
        });

        request.remove(new RemoveHandler() {
            @Override
            public boolean remove(JsonPath path) {
                return false;
            }
        });

        request.replace(new ReplaceHandler() {
            @Override
            public boolean replace(JsonPath path, JsonPatchValue value) {
                return false;
            }
        });

        request.test(new TestHandler() {
            @Override
            public boolean test(JsonPath path, JsonPatchValue value) {
                return false;
            }
        });

        request.apply();

    }
}
