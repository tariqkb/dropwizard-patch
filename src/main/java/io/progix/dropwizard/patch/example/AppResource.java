package io.progix.dropwizard.patch.example;

import io.dropwizard.jersey.PATCH;
import io.progix.dropwizard.patch.api.PatchRequest;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AppResource {

	private List<User> users = Arrays.asList(new User(1, "Mark", "mark@email.com"), new User(2, "Tariq", "tariq@test.com"), new User(3, "Alli",
			"alli@roman.com"));

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
	public void updateUser(PatchRequest request) {

	}
}
