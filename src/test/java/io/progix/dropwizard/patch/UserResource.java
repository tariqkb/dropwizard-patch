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
import java.util.Objects;

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
    public void updateUser(@PathParam("id") int id, PatchRequest request) throws PatchTestFailedException {

        final User user = users.get(id);

        request.add(new AddHandler() {

            @Override
            public void add(JsonPath path, JsonPatchValue value) {
                if (path.property(0).exists()) {
                    if (path.property(0).is("pets")) {
                        if (path.element(1).exists() && path.endsAt(1)) {
                            int petIndex = path.element(1).val();
                            if (!path.property(2).exists()) {
                                user.getPets().addAll(petIndex, value.many(Pet.class));
                            } else {
                                throw new InvalidPatchPathException(path);
                            }
                        } else if (path.endsAt(0)) {
                            user.getPets().addAll(value.many(Pet.class));
                        } else {
                            throw new InvalidPatchPathException(path);
                        }
                    } else if (path.property(0).is("name")) {
                        user.setName(value.one(String.class));
                    } else if (path.property(0).is("emailAddresses")) {
                        if (path.element(1).exists() && path.endsAt(1)) {
                            user.getEmailAddresses().addAll(path.element(1).val(), value.many(String.class));
                        } else if (path.endsAt(0)) {
                            user.getEmailAddresses().addAll(value.many(String.class));
                        } else {
                            throw new InvalidPatchPathException(path);
                        }
                    } else {
                        throw new InvalidPatchPathException(path);
                    }
                } else {
                    throw new InvalidPatchPathException(path);
                }
            }
        });

        request.copy(new CopyHandler() {
            @Override
            public void copy(JsonPath from, JsonPath path) {
                if (from.property(0).is("pets") && path.property(0).is("pets") && from.endsAt(1) && path.endsAt(1)) {
                    if (from.element(1).exists() && path.element(1).exists()) {
                        Pet pet = user.getPets().get(from.element(1).val());
                        user.getPets().add(path.element(1).val(), pet);
                    } else {
                        throw new InvalidPatchPathException(path);
                    }
                } else if (from.property(0).is("emailAddresses") && path.property(0).is("emailAddresses") && from.endsAt(1) && path.endsAt(1)) {
                    if (from.element(1).exists() && path.element(1).exists()) {
                        String emailAddress = user.getEmailAddresses().get(from.element(1).val());
                        user.getEmailAddresses().add(path.element(1).val(), emailAddress);
                    } else {
                        throw new InvalidPatchPathException(path);
                    }
                } else {
                    throw new InvalidPatchPathException(path);
                }
            }
        });

        request.move(new MoveHandler() {
            @Override
            public void move(JsonPath from, JsonPath path) {
                if (from.property(0).is("pets") && path.property(0).is("pets")) {
                    if (from.element(1).exists() && path.element(1).exists() && from.endsAt(1) && path.endsAt(1)) {
                        int fromIndex = from.element(1).val();
                        Pet pet = user.getPets().get(fromIndex);
                        user.getPets().remove(fromIndex);

                        user.getPets().add(path.element(1).val(), pet);
                    } else {
                        throw new InvalidPatchPathException(path);
                    }
                } else if (from.property(0).is("emailAddresses") && path.property(0).is("emailAddresses") && from.endsAt(1) && path.endsAt(1)) {
                    if (from.element(1).exists() && path.element(1).exists()) {
                        int fromIndex = from.element(1).val();
                        String emailAddress = user.getEmailAddresses().get(fromIndex);
                        user.getEmailAddresses().remove(fromIndex);

                        user.getEmailAddresses().add(path.element(1).val(), emailAddress);
                    } else {
                        throw new InvalidPatchPathException(path);
                    }
                } else {
                    throw new InvalidPatchPathException(path);
                }
            }
        });

        request.remove(new RemoveHandler() {
            @Override
            public void remove(JsonPath path) {
                if (path.property(0).is("pets") && path.element(1).exists() && path.endsAt(1)) {
                    user.getPets().remove(path.element(1).val());
                } else if (path.property(0).is("emailAddresses") && path.element(1).exists() && path.endsAt(1)) {
                    user.getEmailAddresses().remove(path.element(1).val());
                } else {
                    throw new InvalidPatchPathException(path);
                }
            }
        });

        request.replace(new ReplaceHandler() {
            @Override
            public void replace(JsonPath path, JsonPatchValue value) {
                if (path.property(0).exists()) {
                    if (path.property(0).is("pets")) {
                        if (path.element(1).exists() && path.endsAt(1)) {
                            int petIndex = path.element(1).val();
                            user.getPets().set(petIndex, value.one(Pet.class));
                        } else if (path.endsAt(0)) {
                            user.setPets(value.many(Pet.class));
                        } else {
                            throw new InvalidPatchPathException(path);
                        }
                    } else if (path.property(0).is("name") && path.endsAt(0)) {
                        user.setName(value.one(String.class));
                    } else if (path.property(0).is("emailAddresses")) {
                        if (path.element(1).exists() && path.endsAt(1)) {
                            user.getEmailAddresses().set(path.element(1).val(), value.one(String.class));
                        } else if (path.endsAt(0)) {
                            user.setEmailAddresses(value.many(String.class));
                        } else {
                            throw new InvalidPatchPathException(path);
                        }
                    } else {
                        throw new InvalidPatchPathException(path);
                    }
                } else {
                    throw new InvalidPatchPathException(path);
                }
            }
        });

        request.test(new TestHandler() {
            @Override
            public boolean test(JsonPath path, JsonPatchValue value) {
                if (path.property(0).exists()) {
                    if (path.property(0).is("pets")) {
                        if (path.element(1).exists() && path.endsAt(1)) {
                            int petIndex = path.element(1).val();
                            return Objects.equals(user.getPets().get(petIndex), value.one(Pet.class));
                        } else if (path.endsAt(0)) {
                            return Objects.equals(user.getPets(), value.many(Pet.class));
                        }
                    } else if (path.property(0).is("name") && path.endsAt(0)) {
                        return Objects.equals(user.getName(), value.one(String.class));
                    } else if (path.property(0).is("emailAddresses")) {
                        if (path.element(1).exists() && path.endsAt(1)) {
                            return Objects.equals(user.getEmailAddresses().get(path.element(1).val()), value.one(String.class));
                        } else if (path.endsAt(0)) {
                            return Objects.equals(user.getEmailAddresses(), value.many(String.class));
                        }
                    }
                }
                throw new InvalidPatchPathException(path);
            }
        });

        request.apply();

    }
}