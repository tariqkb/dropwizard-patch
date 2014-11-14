package io.progix.dropwizard.patch.example;

public class User {

	private long id;
	private String name;
	private String emailAddress;

	public User(long id, String name, String emailAddress) {
		this.id = id;
		this.name = name;
		this.emailAddress = emailAddress;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

}
