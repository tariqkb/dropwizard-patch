package io.progix.dropwizard.patch.example;

import io.dropwizard.jersey.PATCH;
import io.progix.dropwizard.patch.hooks.PatchRequest;
import io.progix.dropwizard.patch.hooks.handlers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonPointer;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AppResource {

	private static final Logger logger = Logger.getLogger(AppResource.class);
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

		List<String> path = new ArrayList<String>();
		JsonPointer pointer = JsonPointer.valueOf("/a/b/c");
		while (pointer.tail() != null) {
			String s = pointer.getMatchingProperty();
			path.add(s);
			pointer = pointer.tail();
		}

		logger.info("TEST: " + path);
		throw new RuntimeException("No user found");
	}

	@PATCH
	@Path("/{id}")
	public void updateUser(PatchRequest request) {

		request.add(new AddHandler() {

			@Override
			public void addElement(JsonPointer path, int index, Object value) {

			}

			@Override
			public void add(JsonPointer path, Object value) {

			}
		});

		request.copy(new CopyHandler() {
			@Override
			public void copy(JsonPointer from, JsonPointer path) {

			}
		});

		request.move(new MoveHandler() {
			@Override
			public void move(JsonPointer from, JsonPointer path) {

			}
		});

		request.remove(new RemoveHandler() {
			@Override
			public void remove(JsonPointer path) {

			}
		});

		request.replace(new ReplaceHandler() {
			@Override
			public void replace(JsonPointer path, Object value) {

			}
		});

		request.test(new TestHandler() {
			@Override
			public void test(JsonPointer path, Object value) {

			}
		});

	}
}
