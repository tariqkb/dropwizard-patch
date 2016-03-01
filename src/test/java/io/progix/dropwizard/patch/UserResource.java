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

package io.progix.dropwizard.patch;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.PATCH;
import io.progix.dropwizard.patch.exception.InvalidPatchPathException;
import io.progix.dropwizard.patch.operations.AddOperation;
import io.progix.dropwizard.patch.operations.CopyOperation;
import io.progix.dropwizard.patch.operations.MoveOperation;
import io.progix.dropwizard.patch.operations.RemoveOperation;
import io.progix.dropwizard.patch.operations.ReplaceOperation;
import io.progix.dropwizard.patch.operations.TestOperation;
import io.progix.dropwizard.patch.operations.contextual.ContextualAddOperation;
import io.progix.dropwizard.patch.operations.contextual.ContextualCopyOperation;
import io.progix.dropwizard.patch.operations.contextual.ContextualMoveOperation;
import io.progix.dropwizard.patch.operations.contextual.ContextualRemoveOperation;
import io.progix.dropwizard.patch.operations.contextual.ContextualReplaceOperation;
import io.progix.dropwizard.patch.operations.contextual.ContextualTestOperation;
import org.apache.log4j.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Objects;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    private static final Logger logger = Logger.getLogger(UserResource.class);

    private ObjectMapper mapper = Jackson.newObjectMapper();
    private UserStore dao;

    public UserResource(UserStore store) {
        this.dao = store;
    }

    @PATCH
    @Path("/default/{id}")
    public void updateUserDefault(@PathParam("id") int id, DefaultJsonPatch<User> request) {
        User user = dao.getUsers().get(id);
        User patchedUser = request.apply(user);

        dao.getUsers().set(id, patchedUser);
    }

	@PATCH
	@Path("/default-with-custom/{id}")
	public void updateUserDefaultWithCustom(@PathParam("id") int id, DefaultJsonPatch<User> request) {
		User user = dao.getUsers().get(id);

		request.setAdd(new ContextualAddOperation<User>() {

			@Override
			public User add(User user, JsonPath path, JsonPatchValue value) {

				//Makes sure new instances are included in the patch [#11]
				user = new User(user);

				if (path.property(0).exists()) {
					if (path.property(0).is("pets")) {
						if (path.element(1).exists() && path.endsAt(1)) {
							if (!path.property(2).exists()) {
								if(path.element(1).isEndOfArray()) {
									user.getPets().addAll(value.many(Pet.class, mapper));
								} else {
									int petIndex = path.element(1).val();
									user.getPets().addAll(petIndex, value.many(Pet.class, mapper));
								}
							} else {
								throw new InvalidPatchPathException(path);
							}
						} else if (path.endsAt(0)) {
							user.getPets().addAll(value.many(Pet.class, mapper));
						} else {
							throw new InvalidPatchPathException(path);
						}
					} else if (path.property(0).is("name")) {
						user.setName(value.one(String.class, mapper));
					} else if (path.property(0).is("emailAddresses")) {
						if (path.element(1).exists() && path.endsAt(1)) {
							user.getEmailAddresses().addAll(path.element(1).val(), value.many(String.class, mapper));
						} else if (path.endsAt(0)) {
							user.getEmailAddresses().addAll(value.many(String.class, mapper));
						} else {
							throw new InvalidPatchPathException(path);
						}
					} else {
						throw new InvalidPatchPathException(path);
					}
				} else {
					throw new InvalidPatchPathException(path);
				}

				return user;
			}
		});

		User patchedUser = request.apply(user);

		dao.getUsers().set(id, patchedUser);
	}

    @PATCH
    @Path("/contextual/no-operations/{id}")
    public void noOpContextual(@PathParam("id") int id, ContextualJsonPatch<User> request) {
        dao.getUsers().set(0, request.apply(dao.getUsers().get(0)));
    }

    @PATCH
    @Path("/contextual/{id}")
    public void updateUserContextual(@PathParam("id") int id, ContextualJsonPatch<User> request) {

        request.setAdd(new ContextualAddOperation<User>() {

            @Override
            public User add(User user, JsonPath path, JsonPatchValue value) {

                //Makes sure new instances are included in the patch [#11]
                user = new User(user);

                if (path.property(0).exists()) {
                    if (path.property(0).is("pets")) {
                        if (path.element(1).exists() && path.endsAt(1)) {
                            if (!path.property(2).exists()) {
                                if(path.element(1).isEndOfArray()) {
                                    user.getPets().addAll(value.many(Pet.class, mapper));
                                } else {
                                    int petIndex = path.element(1).val();
                                    user.getPets().addAll(petIndex, value.many(Pet.class, mapper));
                                }
                            } else {
                                throw new InvalidPatchPathException(path);
                            }
                        } else if (path.endsAt(0)) {
                            user.getPets().addAll(value.many(Pet.class, mapper));
                        } else {
                            throw new InvalidPatchPathException(path);
                        }
                    } else if (path.property(0).is("name")) {
                        user.setName(value.one(String.class, mapper));
                    } else if (path.property(0).is("emailAddresses")) {
                        if (path.element(1).exists() && path.endsAt(1)) {
                            user.getEmailAddresses().addAll(path.element(1).val(), value.many(String.class, mapper));
                        } else if (path.endsAt(0)) {
                            user.getEmailAddresses().addAll(value.many(String.class, mapper));
                        } else {
                            throw new InvalidPatchPathException(path);
                        }
                    } else {
                        throw new InvalidPatchPathException(path);
                    }
                } else {
                    throw new InvalidPatchPathException(path);
                }

                return user;
            }
        });

        request.setCopy(new ContextualCopyOperation<User>() {
            @Override
            public User copy(User user, JsonPath from, JsonPath path) {

				//Makes sure new instances are included in the patch [#11]
				user = new User(user);

                if (from.property(0).is("pets") && path.property(0).is("pets") && from.endsAt(1) && path.endsAt(1)) {
                    if (from.element(1).exists() && path.element(1).exists()) {
                        Pet pet = user.getPets().get(from.element(1).val());
                        user.getPets().add(path.element(1).val(), pet);
                    } else {
                        throw new InvalidPatchPathException(path);
                    }
                } else if (from.property(0).is("emailAddresses") && path.property(0).is("emailAddresses") && from
                        .endsAt(1) && path.endsAt(1)) {
                    if (from.element(1).exists() && path.element(1).exists()) {
                        String emailAddress = user.getEmailAddresses().get(from.element(1).val());
                        user.getEmailAddresses().add(path.element(1).val(), emailAddress);
                    } else {
                        throw new InvalidPatchPathException(path);
                    }
                } else {
                    throw new InvalidPatchPathException(path);
                }

                return user;
            }
        });

        request.setMove(new ContextualMoveOperation<User>() {
            @Override
            public User move(User user, JsonPath from, JsonPath path) {
				//Makes sure new instances are included in the patch [#11]
				user = new User(user);

                if (from.property(0).is("pets") && path.property(0).is("pets")) {
                    if (from.element(1).exists() && path.element(1).exists() && from.endsAt(1) && path.endsAt(1)) {
                        int fromIndex = from.element(1).val();
                        Pet pet = user.getPets().get(fromIndex);
                        user.getPets().remove(fromIndex);

                        user.getPets().add(path.element(1).val(), pet);
                    } else {
                        throw new InvalidPatchPathException(path);
                    }
                } else if (from.property(0).is("emailAddresses") && path.property(0).is("emailAddresses") && from
                        .endsAt(1) && path.endsAt(1)) {
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

                return user;
            }
        });

        request.setRemove(new ContextualRemoveOperation<User>() {
            @Override
            public User remove(User user, JsonPath path) {
                if (path.property(0).is("pets") && path.element(1).exists() && path.endsAt(1)) {
                    user.getPets().remove(path.element(1).val());
                } else if (path.property(0).is("emailAddresses") && path.element(1).exists() && path.endsAt(1)) {
                    user.getEmailAddresses().remove(path.element(1).val());
                } else {
                    throw new InvalidPatchPathException(path);
                }
                return user;
            }
        });

        request.setReplace(new ContextualReplaceOperation<User>() {
            @Override
            public User replace(User user, JsonPath path, JsonPatchValue value) {
                if (path.property(0).exists()) {
                    if (path.property(0).is("pets")) {
                        if (path.element(1).exists() && path.endsAt(1)) {
                            int petIndex = path.element(1).val();
                            user.getPets().set(petIndex, value.one(Pet.class, mapper));
                        } else if (path.endsAt(0)) {
                            user.setPets(value.many(Pet.class, mapper));
                        } else {
                            throw new InvalidPatchPathException(path);
                        }
                    } else if (path.property(0).is("name") && path.endsAt(0)) {
                        user.setName(value.one(String.class, mapper));
                    } else if (path.property(0).is("emailAddresses")) {
                        if (path.element(1).exists() && path.endsAt(1)) {
                            user.getEmailAddresses().set(path.element(1).val(), value.one(String.class, mapper));
                        } else if (path.endsAt(0)) {
                            user.setEmailAddresses(value.many(String.class, mapper));
                        } else {
                            throw new InvalidPatchPathException(path);
                        }
                    } else {
                        throw new InvalidPatchPathException(path);
                    }
                } else {
                    throw new InvalidPatchPathException(path);
                }

                return user;
            }
        });

        request.setTest(new ContextualTestOperation<User>() {
            @Override
            public boolean test(User user, JsonPath path, JsonPatchValue value) {
                if (path.property(0).exists()) {
                    if (path.property(0).is("pets")) {
                        if (path.element(1).exists() && path.endsAt(1)) {
                            int petIndex = path.element(1).val();
                            return Objects.equals(user.getPets().get(petIndex), value.one(Pet.class, mapper));
                        } else if (path.endsAt(0)) {
                            return Objects.equals(user.getPets(), value.many(Pet.class, mapper));
                        }
                    } else if (path.property(0).is("name") && path.endsAt(0)) {
                        return Objects.equals(user.getName(), value.one(String.class, mapper));
                    } else if (path.property(0).is("emailAddresses")) {
                        if (path.element(1).exists() && path.endsAt(1)) {
                            return Objects.equals(user.getEmailAddresses().get(path.element(1).val()),
                                    value.one(String.class, mapper));
                        } else if (path.endsAt(0)) {
                            return Objects.equals(user.getEmailAddresses(), value.many(String.class, mapper));
                        }
                    }
                }
                throw new InvalidPatchPathException(path);
            }
        });

        User user = dao.getUsers().get(id);
        User patchedUser = request.apply(user);
        dao.getUsers().set(id, patchedUser);
    }

    @PATCH
    @Path("/no-operations/{id}")
    public void noOp(@PathParam("id") int id, BasicJsonPatch request) {
        request.apply();
    }

    @PATCH
    @Path("/{id}")
    public void updateUser(@PathParam("id") int id, BasicJsonPatch request) {

        final User user = dao.getUsers().get(id);

        request.setAdd(new AddOperation() {

            @Override
            public void add(JsonPath path, JsonPatchValue value) {
                if (path.property(0).exists()) {
                    if (path.property(0).is("pets")) {
                        if (path.element(1).exists() && path.endsAt(1)) {
                            if (!path.property(2).exists()) {
								if(path.element(1).isEndOfArray()) {
									user.getPets().addAll(value.many(Pet.class, mapper));
								} else {
									int petIndex = path.element(1).val();
									user.getPets().addAll(petIndex, value.many(Pet.class, mapper));
								}
                            } else {
                                throw new InvalidPatchPathException(path);
                            }
                        } else if (path.endsAt(0)) {
                            user.getPets().addAll(value.many(Pet.class, mapper));
                        } else {
                            throw new InvalidPatchPathException(path);
                        }
                    } else if (path.property(0).is("name")) {
                        user.setName(value.one(String.class, mapper));
                    } else if (path.property(0).is("emailAddresses")) {
                        if (path.element(1).exists() && path.endsAt(1)) {
                            user.getEmailAddresses().addAll(path.element(1).val(), value.many(String.class, mapper));
                        } else if (path.endsAt(0)) {
                            user.getEmailAddresses().addAll(value.many(String.class, mapper));
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

        request.setCopy(new CopyOperation() {
            @Override
            public void copy(JsonPath from, JsonPath path) {
                if (from.property(0).is("pets") && path.property(0).is("pets") && from.endsAt(1) && path.endsAt(1)) {
                    if (from.element(1).exists() && path.element(1).exists()) {
                        Pet pet = user.getPets().get(from.element(1).val());
                        user.getPets().add(path.element(1).val(), pet);
                    } else {
                        throw new InvalidPatchPathException(path);
                    }
                } else if (from.property(0).is("emailAddresses") && path.property(0).is("emailAddresses") && from
                        .endsAt(1) && path.endsAt(1)) {
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

        request.setMove(new MoveOperation() {
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
                } else if (from.property(0).is("emailAddresses") && path.property(0).is("emailAddresses") && from
                        .endsAt(1) && path.endsAt(1)) {
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

        request.setRemove(new RemoveOperation() {
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

        request.setReplace(new ReplaceOperation() {
            @Override
            public void replace(JsonPath path, JsonPatchValue value) {
                if (path.property(0).exists()) {
                    if (path.property(0).is("pets")) {
                        if (path.element(1).exists() && path.endsAt(1)) {
                            int petIndex = path.element(1).val();
                            user.getPets().set(petIndex, value.one(Pet.class, mapper));
                        } else if (path.endsAt(0)) {
                            user.setPets(value.many(Pet.class, mapper));
                        } else {
                            throw new InvalidPatchPathException(path);
                        }
                    } else if (path.property(0).is("name") && path.endsAt(0)) {
                        user.setName(value.one(String.class, mapper));
                    } else if (path.property(0).is("emailAddresses")) {
                        if (path.element(1).exists() && path.endsAt(1)) {
                            user.getEmailAddresses().set(path.element(1).val(), value.one(String.class, mapper));
                        } else if (path.endsAt(0)) {
                            user.setEmailAddresses(value.many(String.class, mapper));
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

        request.setTest(new TestOperation() {
            @Override
            public boolean test(JsonPath path, JsonPatchValue value) {
                if (path.property(0).exists()) {
                    if (path.property(0).is("pets")) {
                        if (path.element(1).exists() && path.endsAt(1)) {
                            int petIndex = path.element(1).val();
                            return Objects.equals(user.getPets().get(petIndex), value.one(Pet.class, mapper));
                        } else if (path.endsAt(0)) {
                            return Objects.equals(user.getPets(), value.many(Pet.class, mapper));
                        }
                    } else if (path.property(0).is("name") && path.endsAt(0)) {
                        return Objects.equals(user.getName(), value.one(String.class, mapper));
                    } else if (path.property(0).is("emailAddresses")) {
                        if (path.element(1).exists() && path.endsAt(1)) {
                            return Objects.equals(user.getEmailAddresses().get(path.element(1).val()),
                                    value.one(String.class, mapper));
                        } else if (path.endsAt(0)) {
                            return Objects.equals(user.getEmailAddresses(), value.many(String.class, mapper));
                        }
                    }
                }
                throw new InvalidPatchPathException(path);
            }
        });

        request.apply();

    }
}