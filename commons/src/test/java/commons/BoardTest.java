package commons;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class BoardTest {

    @Test
    public void constructorTest() {
        var b = new Board("banana");
        assertEquals(b.getTitle(), "banana");
    }

    @Test
    public void setTitlePositiveTest() {
        var b = new Board("banana");
        b.setTitle("apple");
        assertEquals(b.getTitle(), "apple");
    }

    @Test
    public void setTitleNegativeTest() {
        var b = new Board("banana");
        b.setTitle("apple");
        assertNotEquals(b.getTitle(), "banana");
    }

    @Test
    public void setListsPositiveTest() {
        var b = new Board("banana");
        var t = new CardList("apple");

        b.setLists(List.of(t));
        assertEquals(b.getLists(), List.of(t));
    }

    @Test
    public void setListsNegativeTest() {
        var b = new Board("banana");
        var t = new CardList("apple");

        b.setLists(List.of(t));
        assertNotEquals(b.getLists(), List.of(new CardList("banana")));
    }

    @Test
    public void setIdPositiveTest() {
        var b = new Board("banana");
        b.setId(1);
        assertEquals(b.getId(), 1);
    }

    @Test
    public void setIdNegativeTest() {
        var b = new Board("banana");
        b.setId(1);
        assertNotEquals(b.getId(), 2);
    }

    @Test
    public void equalsTest() {
        var b1 = new Board("banana");
        var b2 = new Board("banana");
        assertEquals(b1, b2);
    }

    @Test
    public void equalsTest2() {
        var b1 = new Board("banana");
        var b2 = new Board("apple");
        assertNotEquals(b1, b2);
    }

    @Test
    public void equalsTest3() {
        var b1 = new Board("banana");
        var b2 = new Board("banana");
        b2.setLists(List.of(new CardList("apple")));
        assertNotEquals(b1, b2);
    }

    @Test
    public void hashCodeTest() {
        var b1 = new Board("banana");
        var b2 = new Board("banana");
        assertEquals(b1.hashCode(), b2.hashCode());

        var b3 = new Board("apple");
        b3.setTitle("banana");
        assertEquals(b1.hashCode(), b3.hashCode());
    }

    @Test
    public void cloneTest() throws CloneNotSupportedException {
        var b1 = new Board("banana");
        b1.add(new CardList("apple"));
        b1.getTags().add(new Tag("banana2"));

        var b2 = b1.clone();
        assertEquals(b1, b2);
    }
}
