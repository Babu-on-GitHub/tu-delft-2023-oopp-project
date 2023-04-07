package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BoardIdWithColorsTest {

    @Test
    void constructorTest() {
        var b = new BoardIdWithColors();
        assertNotNull(b);
    }

    @Test
    void getBoardId() {
        var b = new BoardIdWithColors(1L);
        assertEquals(b.getBoardId(), 1L);
    }

    @Test
    void setBoardId() {
        var b = new BoardIdWithColors();
        b.setBoardId(1L);
        assertEquals(b.getBoardId(), 1L);
    }

    @Test
    void getBoardPair() {
        var b = new BoardIdWithColors(1L);
        var p = new ColorPair();
        b.setBoardPair(p);
        assertEquals(b.getBoardPair(), p);
    }

    @Test
    void setBoardPair() {
        var b = new BoardIdWithColors(2L);
        var p = new ColorPair();
        b.setBoardPair(p);
        assertEquals(b.getBoardPair(), p);
    }

    @Test
    void testEquals() {
        var b = new BoardIdWithColors(1L);
        var b2 = new BoardIdWithColors(1L);
        assertEquals(b, b2);

        var b3 = new BoardIdWithColors(2L);
        assertNotEquals(b, b3);

        b3.setBoardId(1L);
        assertEquals(b, b3);
    }

    @Test
    void testHashCode() {
        var b = new BoardIdWithColors(1L);
        var b2 = new BoardIdWithColors(1L);
        assertEquals(b.hashCode(), b2.hashCode());

        var b3 = new BoardIdWithColors(2L);
        assertNotEquals(b.hashCode(), b3.hashCode());

        b3.setBoardId(1L);
        assertEquals(b.hashCode(), b3.hashCode());
    }
}
