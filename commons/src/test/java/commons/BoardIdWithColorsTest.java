package commons;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BoardIdWithColorsTest {

    @Test
    void constructorTest() {
        var b = new BoardIdWithColors(1);
        assertNotNull(b);
    }

    @Test
    void getBoardId() {
        var b = new BoardIdWithColors(1L);
        assertEquals(b.getBoardId(), 1L);
    }

    @Test
    void setBoardId() {
        var b = new BoardIdWithColors(2);
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

    @Test
    void cloneTest() {
        var b = new BoardIdWithColors(1L);
        try {
            var b2 = b.clone();
            assertEquals(b, b2);
        } catch (CloneNotSupportedException e) {
            fail();
        }
    }

    @Test
    void setCardHighlightColor() {
        var b = new BoardIdWithColors(1L);
        var map = new HashMap<Long, ColorPair>();
        map.put(1L, new ColorPair());
        b.setCardHighlightColors(map);
        assertEquals(map, b.getCardHighlightColors());
    }

    @Test
    void setListPairTest() {
        var b = new BoardIdWithColors(1L);
        var pair = new ColorPair();
        b.setListPair(pair);
        assertEquals(pair, b.getListPair());
    }

    @Test
    void setCardPairTest() {
        var b = new BoardIdWithColors(1L);
        var pair = new ColorPair();
        b.setCardPair(pair);
        assertEquals(pair, b.getCardPair());
    }
}
