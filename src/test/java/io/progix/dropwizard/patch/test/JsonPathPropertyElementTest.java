package io.progix.dropwizard.patch.test;

import com.fasterxml.jackson.core.JsonPointer;
import io.progix.dropwizard.patch.JsonPath;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class JsonPathPropertyElementTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

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
    public void propertyIsAC2() {
        assertTrue(ac2.property(0).is("a"));
        assertFalse(ac2.property(0).is("d"));
        assertFalse(ac2.property(0).is("-"));
        assertFalse(ac2.property(0).is("2"));

        assertTrue(ac2.property(1).is("c"));
        assertFalse(ac2.property(1).is("d"));
        assertFalse(ac2.property(1).is("-"));
        assertFalse(ac2.property(1).is("2"));

        assertFalse(ac2.property(2).is("c"));
        assertFalse(ac2.property(2).is("d"));
        assertFalse(ac2.property(2).is("-"));
        assertFalse(ac2.property(2).is("2"));

        assertFalse(ac2.property(3).is("a"));
        assertFalse(ac2.property(3).is("d"));
        assertFalse(ac2.property(3).is("-"));
        assertFalse(ac2.property(3).is("2"));

        assertFalse(ac2.property(4).is("a"));
        assertFalse(ac2.property(4).is("d"));
        assertFalse(ac2.property(4).is("-"));
        assertFalse(ac2.property(4).is("2"));
    }

    @Test
    public void propertyIsAB() {
        assertTrue(ab.property(0).is("a"));
        assertFalse(ab.property(0).is("d"));
        assertFalse(ab.property(0).is("-"));
        assertFalse(ab.property(0).is("2"));

        assertTrue(ab.property(1).is("b"));
        assertFalse(ab.property(1).is("d"));
        assertFalse(ab.property(1).is("-"));
        assertFalse(ab.property(1).is("2"));

        assertFalse(ab.property(2).is("a"));
        assertFalse(ab.property(2).is("d"));
        assertFalse(ab.property(2).is("-"));
        assertFalse(ab.property(2).is("2"));

        assertFalse(ab.property(3).is("a"));
        assertFalse(ab.property(3).is("d"));
        assertFalse(ab.property(3).is("-"));
        assertFalse(ab.property(3).is("2"));
    }

    @Test
    public void propertyIsACEnd() {
        assertTrue(acend.property(0).is("a"));
        assertFalse(acend.property(0).is("d"));
        assertFalse(acend.property(0).is("-"));
        assertFalse(acend.property(0).is("2"));

        assertTrue(acend.property(1).is("c"));
        assertFalse(acend.property(1).is("d"));
        assertFalse(acend.property(1).is("-"));
        assertFalse(acend.property(1).is("2"));

        assertFalse(acend.property(2).is("c"));
        assertFalse(acend.property(2).is("d"));
        assertFalse(acend.property(2).is("-"));
        assertFalse(acend.property(2).is("2"));

        assertFalse(acend.property(3).is("a"));
        assertFalse(acend.property(3).is("d"));
        assertFalse(acend.property(3).is("-"));
        assertFalse(acend.property(3).is("2"));

        assertFalse(acend.property(4).is("a"));
        assertFalse(acend.property(4).is("d"));
        assertFalse(acend.property(4).is("-"));
        assertFalse(acend.property(4).is("2"));
    }

    @Test
    public void propertyIsAPound() {
        assertTrue(apound.property(0).is("a"));
        assertFalse(apound.property(0).is("d"));
        assertFalse(apound.property(0).is("-"));
        assertFalse(apound.property(0).is("2"));

        assertTrue(apound.property(1).is("#"));
        assertFalse(apound.property(1).is("d"));
        assertFalse(apound.property(1).is("-"));
        assertFalse(apound.property(1).is("2"));

        assertFalse(apound.property(2).is("a"));
        assertFalse(apound.property(2).is("d"));
        assertFalse(apound.property(2).is("-"));
        assertFalse(apound.property(2).is("2"));

        assertFalse(apound.property(3).is("a"));
        assertFalse(apound.property(3).is("d"));
        assertFalse(apound.property(3).is("-"));
        assertFalse(apound.property(3).is("2"));
    }

    @Test
    public void propertyValAC2() {
        assertEquals(ac2.property(0).val(), "a");
        assertNotEquals(ac2.property(0).val(), "d");
        assertNotEquals(ac2.property(0).val(), "-");
        assertNotEquals(ac2.property(0).val(), "2");

        assertEquals(ac2.property(1).val(), "c");
        assertNotEquals(ac2.property(1).val(), "d");
        assertNotEquals(ac2.property(1).val(), "-");
        assertNotEquals(ac2.property(1).val(), "2");

        assertNotEquals(ac2.property(2).val(), "c");
        assertNotEquals(ac2.property(2).val(), "d");
        assertNotEquals(ac2.property(2).val(), "-");
        assertNotEquals(ac2.property(2).val(), "2");
        assertNull(ac2.property(2).val());

        assertNotEquals(ac2.property(3).val(), "a");
        assertNotEquals(ac2.property(3).val(), "d");
        assertNotEquals(ac2.property(3).val(), "-");
        assertNotEquals(ac2.property(3).val(), "2");
        assertNull(ac2.property(3).val());

        assertNotEquals(ac2.property(4).val(), "a");
        assertNotEquals(ac2.property(4).val(), "d");
        assertNotEquals(ac2.property(4).val(), "-");
        assertNotEquals(ac2.property(4).val(), "2");
        assertNull(ac2.property(4).val());
    }

    @Test
    public void propertyValAB() {
        assertEquals(ab.property(0).val(), "a");
        assertNotEquals(ab.property(0).val(), "d");
        assertNotEquals(ab.property(0).val(), "-");
        assertNotEquals(ab.property(0).val(), "2");

        assertEquals(ab.property(1).val(), "b");
        assertNotEquals(ab.property(1).val(), "d");
        assertNotEquals(ab.property(1).val(), "-");
        assertNotEquals(ab.property(1).val(), "2");

        assertNotEquals(ab.property(2).val(), "a");
        assertNotEquals(ab.property(2).val(), "d");
        assertNotEquals(ab.property(2).val(), "-");
        assertNotEquals(ab.property(2).val(), "2");
        assertNull(ab.property(2).val());

        assertNotEquals(ab.property(3).val(), "a");
        assertNotEquals(ab.property(3).val(), "d");
        assertNotEquals(ab.property(3).val(), "-");
        assertNotEquals(ab.property(3).val(), "2");
        assertNull(ab.property(3).val());
    }

    @Test
    public void propertyValACEnd() {
        assertEquals(acend.property(0).val(), "a");
        assertNotEquals(acend.property(0).val(), "d");
        assertNotEquals(acend.property(0).val(), "-");
        assertNotEquals(acend.property(0).val(), "2");

        assertEquals(acend.property(1).val(), "c");
        assertNotEquals(acend.property(1).val(), "d");
        assertNotEquals(acend.property(1).val(), "-");
        assertNotEquals(acend.property(1).val(), "2");

        assertNotEquals(acend.property(2).val(), "c");
        assertNotEquals(acend.property(2).val(), "d");
        assertNotEquals(acend.property(2).val(), "-");
        assertNotEquals(acend.property(2).val(), "2");
        assertNull(acend.property(2).val());

        assertNotEquals(acend.property(3).val(), "a");
        assertNotEquals(acend.property(3).val(), "d");
        assertNotEquals(acend.property(3).val(), "-");
        assertNotEquals(acend.property(3).val(), "2");
        assertNull(acend.property(3).val());

        assertNotEquals(acend.property(4).val(), "a");
        assertNotEquals(acend.property(4).val(), "d");
        assertNotEquals(acend.property(4).val(), "-");
        assertNotEquals(acend.property(4).val(), "2");
        assertNull(acend.property(4).val());
    }

    @Test
    public void propertyValAPound() {
        assertEquals(apound.property(0).val(), "a");
        assertNotEquals(apound.property(0).val(), "d");
        assertNotEquals(apound.property(0).val(), "-");
        assertNotEquals(apound.property(0).val(), "2");

        assertEquals(apound.property(1).val(), "#");
        assertNotEquals(apound.property(1).val(), "d");
        assertNotEquals(apound.property(1).val(), "-");
        assertNotEquals(apound.property(1).val(), "2");

        assertNotEquals(apound.property(2).val(), "a");
        assertNotEquals(apound.property(2).val(), "d");
        assertNotEquals(apound.property(2).val(), "-");
        assertNotEquals(apound.property(2).val(), "2");
        assertNull(apound.property(2).val());

        assertNotEquals(apound.property(3).val(), "a");
        assertNotEquals(apound.property(3).val(), "d");
        assertNotEquals(apound.property(3).val(), "-");
        assertNotEquals(apound.property(3).val(), "2");
        assertNull(apound.property(3).val());

    }

    @Test
    public void propertyExists() {
        assertTrue(ab.property(0).exists());
        assertTrue(ab.property(1).exists());
        assertFalse(ab.property(2).exists());
        assertFalse(ab.property(3).exists());

        assertTrue(apound.property(0).exists());
        assertTrue(apound.property(1).exists());
        assertFalse(apound.property(2).exists());
        assertFalse(apound.property(3).exists());

        assertTrue(acend.property(0).exists());
        assertTrue(acend.property(1).exists());
        assertFalse(acend.property(2).exists());
        assertFalse(acend.property(3).exists());

        assertTrue(ac2.property(0).exists());
        assertTrue(ac2.property(1).exists());
        assertFalse(ac2.property(2).exists());
        assertFalse(ac2.property(3).exists());
    }

    @Test
    public void elementIsAC2() {
        assertFalse(ac2.element(0).is(2));
        assertFalse(ac2.element(1).is(2));
        assertTrue(ac2.element(2).is(2));
        assertFalse(ac2.element(2).is(3));
        assertFalse(ac2.element(3).is(2));
        assertFalse(ac2.element(4).is(2));

    }

    @Test
    public void elementIsAPound() {
        assertFalse(apound.element(0).is(2));
        assertFalse(apound.element(1).is(2));
        assertFalse(apound.element(2).is(2));
        assertFalse(apound.element(3).is(2));
    }

    @Test
    public void elementIsACEnd() {
        assertFalse(acend.element(0).is(2));
        assertFalse(acend.element(1).is(2));
        assertFalse(acend.element(2).is(2));
        assertFalse(acend.element(3).is(2));
        assertFalse(acend.element(4).is(2));
    }

    @Test
    public void elementIsAB() {
        assertFalse(ab.element(0).is(2));
        assertFalse(ab.element(1).is(2));
        assertFalse(ab.element(2).is(2));
        assertFalse(ab.element(3).is(2));
    }

    @Test
    public void elementValAC2() {
        assertNotNull(ac2.element(2).val());
        try {
            ac2.element(0).val();
            fail("Should have thrown null pointer");
        } catch (NullPointerException ignored) {

        }
        try {
            ac2.element(1).val();
            fail("Should have thrown null pointer");
        } catch (NullPointerException ignored) {

        }
        try {
            ac2.element(3).val();
            fail("Should have thrown null pointer");
        } catch (NullPointerException ignored) {

        }
        try {
            ac2.element(4).val();
            fail("Should have thrown null pointer");
        } catch (NullPointerException ignored) {

        }
    }

    @Test
    public void elementValAPound() {
        try {
            apound.element(0).val();
            fail("Should have thrown null pointer");
        } catch (NullPointerException ignored) {

        }
        try {
            apound.element(1).val();
            fail("Should have thrown null pointer");
        } catch (NullPointerException ignored) {

        }
        try {
            apound.element(2).val();
            fail("Should have thrown null pointer");
        } catch (NullPointerException ignored) {

        }
        try {
            apound.element(3).val();
            fail("Should have thrown null pointer");
        } catch (NullPointerException ignored) {

        }
    }

    @Test
    public void elementValACEnd() {
        try {
            acend.element(0).val();
            fail("Should have thrown null pointer");
        } catch (NullPointerException ignored) {

        }
        try {
            acend.element(1).val();
            fail("Should have thrown null pointer");
        } catch (NullPointerException ignored) {

        }
        try {
            acend.element(2).val();
            fail("Should have thrown null pointer");
        } catch (NullPointerException ignored) {

        }
        try {
            acend.element(3).val();
            fail("Should have thrown null pointer");
        } catch (NullPointerException ignored) {

        }
        try {
            acend.element(4).val();
            fail("Should have thrown null pointer");
        } catch (NullPointerException ignored) {

        }
    }

    @Test
    public void elementValAB() {

        try {
            ab.element(0).val();
            fail("Should have thrown null pointer");
        } catch (NullPointerException ignored) {

        }
        try {
            ab.element(1).val();
            fail("Should have thrown null pointer");
        } catch (NullPointerException ignored) {

        }
        try {
            ab.element(2).val();
            fail("Should have thrown null pointer");
        } catch (NullPointerException ignored) {

        }
        try {
            ab.element(3).val();
            fail("Should have thrown null pointer");
        } catch (NullPointerException ignored) {

        }
    }

    @Test
    public void elementExistsAC2() {
        assertFalse(ac2.element(0).exists());
        assertTrue(ac2.element(2).exists());
        assertFalse(ac2.element(1).exists());
        assertFalse(ac2.element(3).exists());
        assertFalse(ac2.element(4).exists());
    }

    @Test
    public void elementExistsAPound() {
        assertFalse(apound.element(0).exists());
        assertFalse(apound.element(1).exists());
        assertFalse(apound.element(2).exists());
        assertFalse(apound.element(3).exists());
    }

    @Test
    public void elementExistsACEnd() {
        assertFalse(acend.element(1).exists());
        assertFalse(acend.element(0).exists());
        assertFalse(acend.element(3).exists());
        assertTrue(acend.element(2).exists());
        assertFalse(acend.element(4).exists());
    }

    @Test
    public void elementExistsAB() {
        assertFalse(ab.element(0).exists());
        assertFalse(ab.element(1).exists());
        assertFalse(ab.element(2).exists());
        assertFalse(ab.element(3).exists());
    }

    @Test
    public void elementEndOfArrayAC2() {
        assertFalse(ac2.element(1).isEndOfArray());
        assertFalse(ac2.element(2).isEndOfArray());
        assertFalse(ac2.element(3).isEndOfArray());
        assertFalse(ac2.element(0).isEndOfArray());
        assertFalse(ac2.element(4).isEndOfArray());
    }

    @Test
    public void elementEndOfArrayAPound() {
        assertFalse(apound.element(0).isEndOfArray());
        assertFalse(apound.element(1).isEndOfArray());
        assertFalse(apound.element(2).isEndOfArray());
        assertFalse(apound.element(3).isEndOfArray());
    }

    @Test
    public void elementEndOfArrayACEnd() {
        assertFalse(acend.element(0).isEndOfArray());
        assertFalse(acend.element(1).isEndOfArray());
        assertTrue(acend.element(2).isEndOfArray());
        assertFalse(acend.element(3).isEndOfArray());
        assertFalse(acend.element(4).isEndOfArray());
    }

    @Test
    public void elementEndOfArrayAB() {
        assertFalse(ab.element(0).isEndOfArray());
        assertFalse(ab.element(1).isEndOfArray());
        assertFalse(ab.element(2).isEndOfArray());
        assertFalse(ab.element(3).isEndOfArray());
    }

}
