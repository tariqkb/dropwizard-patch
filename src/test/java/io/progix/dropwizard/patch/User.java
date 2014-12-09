package io.progix.dropwizard.patch;

import java.util.List;

public class User {
    private long id;
    private String name;
    private List<String> emailAddresses;
    private List<Pet> pets;

    public User(long id, String name, List<String> emailAddresses, List<Pet> pets) {
        this.id = id;
        this.name = name;
        this.emailAddresses = emailAddresses;
        this.pets = pets;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getEmailAddresses() {
        return emailAddresses;
    }

    public List<Pet> getPets() {
        return pets;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmailAddresses(List<String> emailAddresses) {
        this.emailAddresses = emailAddresses;
    }

    public void setPets(List<Pet> pets) {
        this.pets = pets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != user.id) return false;
        if (!emailAddresses.equals(user.emailAddresses)) return false;
        if (!name.equals(user.name)) return false;
        if (!pets.equals(user.pets)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + name.hashCode();
        result = 31 * result + emailAddresses.hashCode();
        result = 31 * result + pets.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", emailAddresses=" + emailAddresses +
                ", pets=" + pets +
                '}';
    }
}
