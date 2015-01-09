package io.progix.dropwizard.patch.test;

import com.fasterxml.jackson.core.JsonPointer;
import io.progix.dropwizard.patch.JsonPath;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class JsonPathTest {

    JsonPath ab;
    JsonPath acend;
    JsonPath ac2;
    JsonPath apound;

    @Before
    public void init() {
        ab = new JsonPath(JsonPointer.compile("/a/b"));
        acend = new JsonPath(JsonPointer.compile("/a/c/-"));
        ac2 = new JsonPath(JsonPointer.compile("/a/c/2"));
        apound = new JsonPath(JsonPointer.compile("/a/#"));
    }

    @Test
    public void endsAtTest() {
        assertFalse(ab.endsAt(0));
        assertTrue(ab.endsAt(1));
        assertFalse(ab.endsAt(2));

        assertFalse(apound.endsAt(0));
        assertTrue(apound.endsAt(1));
        assertFalse(apound.endsAt(2));

        assertFalse(acend.endsAt(0));
        assertFalse(acend.endsAt(1));
        assertTrue(acend.endsAt(2));
        assertFalse(acend.endsAt(3));

        assertFalse(ac2.endsAt(0));
        assertFalse(ac2.endsAt(1));
        assertTrue(ac2.endsAt(2));
        assertFalse(ac2.endsAt(3));
    }

    @Test
    public void isPathTest() {
        assertTrue(ab.is("/a/b"));
        assertFalse(ab.is("/a/c"));
        assertFalse(ab.is("/a/2"));
        assertFalse(ab.is("/a"));
        assertFalse(ab.is("/a/#"));
        assertFalse(ab.is("/a/~#"));
        assertFalse(ab.is("/a/b/c"));
        assertFalse(ab.is("/a/b/2"));
        assertFalse(ab.is("/a/b/#"));
        assertFalse(ab.is("/a/b/~#"));

        assertTrue(apound.is("/a/~#"));
        assertFalse(apound.is("/a/c"));
        assertFalse(apound.is("/a/2"));
        assertFalse(apound.is("/a"));
        assertFalse(apound.is("/a/#"));
        assertFalse(apound.is("/a/b/c"));
        assertFalse(apound.is("/a/b/2"));
        assertFalse(apound.is("/a/b/#"));
        assertFalse(apound.is("/a/b/~#"));

        assertTrue(acend.is("/a/c/-"));
        assertFalse(acend.is("/a/b"));
        assertFalse(acend.is("/a/c"));
        assertFalse(acend.is("/a/2"));
        assertFalse(acend.is("/a"));
        assertFalse(acend.is("/a/#"));
        assertFalse(acend.is("/a/~#"));
        assertFalse(acend.is("/a/b/c"));
        assertFalse(acend.is("/a/b/2"));
        assertFalse(acend.is("/a/b/#"));
        assertFalse(acend.is("/a/b/~#"));

        assertTrue(ac2.is("/a/c/2"));
        assertTrue(ac2.is("/a/c/#"));
        assertFalse(ac2.is("/a/b"));
        assertFalse(ac2.is("/a/c"));
        assertFalse(ac2.is("/a/2"));
        assertFalse(ac2.is("/a"));
        assertFalse(ac2.is("/a/#"));
        assertFalse(ac2.is("/a/~#"));
        assertFalse(ac2.is("/a/b/c"));
        assertFalse(ac2.is("/a/b/2"));
        assertFalse(ac2.is("/a/b/#"));
        assertFalse(ac2.is("/a/b/~#"));
    }

}
