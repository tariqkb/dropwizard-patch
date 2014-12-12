package io.progix.dropwizard.patch;

import java.util.List;

public class Pet {

    private long id;
    private int age;
    private List<String> nicknames;

    public Pet(long id, int age, List<String> nicknames) {
        this.id = id;
        this.age = age;
        this.nicknames = nicknames;
    }

    public Pet() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<String> getNicknames() {
        return nicknames;
    }

    public void setNicknames(List<String> nicknames) {
        this.nicknames = nicknames;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Pet pet = (Pet) o;

        if (age != pet.age)
            return false;
        if (id != pet.id)
            return false;
        if (!nicknames.equals(pet.nicknames))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + age;
        result = 31 * result + nicknames.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "id=" + id +
                ", age=" + age +
                ", nicknames=" + nicknames +
                '}';
    }
}
