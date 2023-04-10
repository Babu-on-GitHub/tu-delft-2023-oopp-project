package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ColorPairTest {

    @Test
    void constructorTest() {
        var c = new ColorPair("#111111", "#111112");
        assertEquals(c.getBackground(), "#111111");
        assertEquals(c.getFont(), "#111112");
    }

    @Test
    void defaultConstructorTest() {
        var c = new ColorPair();
        assertNotNull(c);
    }

    @Test
    void setBackgroundTest() {
        var c = new ColorPair();
        c.setBackground("#111111");
        assertEquals(c.getBackground(), "#111111");
    }

    @Test
    void setFontTest() {
        var c = new ColorPair();
        c.setFont("#111111");
        assertEquals(c.getFont(), "#111111");
    }

    @Test
    void equalsPositiveTest() {
        var c = new ColorPair("#111111", "#111112");
        var c2 = new ColorPair("#111111", "#111112");
        assertEquals(c, c2);

        var c3 = new ColorPair("#111111", "#333333");
        c3.setFont("#111112");
        assertEquals(c, c3);
    }

    @Test
    void hashCodeTest() {
        var c = new ColorPair("#111111", "#111112");
        var c2 = new ColorPair("#111111", "#111112");
        assertEquals(c.hashCode(), c2.hashCode());

        var c3 = new ColorPair("#111333", "#111112");
        assertNotEquals(c.hashCode(), c3.hashCode());
    }
}
