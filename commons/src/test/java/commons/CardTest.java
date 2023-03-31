package commons;

import org.junit.jupiter.api.Test;

//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;


public class CardTest {

    @Test
    public void constructorTest() {
        var c = new Card("banana");
        assertEquals(c.getTitle(), "banana");
    }

    @Test
    public void setTitlePositiveTest() {
        var c = new Card("banana");
        c.setTitle("apple");
        assertEquals(c.getTitle(), "apple");
    }

    @Test
    public void setTitleNegativeTest() {
        var c = new Card("banana");
        c.setTitle("apple");
        assertNotEquals(c.getTitle(), "banana");
    }

    @Test
    public void setDescriptionPositiveTest() {
        var c = new Card("banana");
        c.setDescription("apple");
        assertEquals(c.getDescription(), "apple");
    }

    @Test
    public void setDescriptionNegativeTest() {
        var c = new Card("banana");
        c.setDescription("apple");
        assertNotEquals(c.getDescription(), "banana");
    }

    @Test
    public void equalsPositiveTest() {
        var c = new Card("banana");
        var c2 = new Card("banana");
        assertEquals(c, c2);
    }

    @Test
    public void equalsNegativeTest() {
        var c = new Card("banana");
        var c2 = new Card("apple");
        assertNotEquals(c, c2);
    }

    @Test
    public void equalsNullTest() {
        var c = new Card("banana");
        assertNotEquals(c, null);
    }

    @Test
    public void hashCodeTest() {
        var c = new Card("banana");
        var c2 = new Card("banana");
        assertEquals(c.hashCode(), c2.hashCode());

        var c3 = new Card("apple");
        c3.setTitle("banana");
        assertEquals(c.hashCode(), c3.hashCode());
    }

    @Test
    public void timestampTest() {
        var c = new Card("pear");
        var c2 = new Card("pear");
        assertEquals(c.getTimestamp(), c2.getTimestamp());
    }

    @Test
    public void syncTest() {
        var c = new Card("orange");
        c.sync();
        var c2 = new Card("orange");
        c2.sync();

        assertFalse(c.getTimestamp().after(c2.getTimestamp()));
    }
}
