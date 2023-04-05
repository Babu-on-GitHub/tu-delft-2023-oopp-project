package commons;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class CardListTest {

    @Test
    public void constructorTest() {
        var c = new CardList("banana");
        assertEquals(c.getTitle(), "banana");
    }

    @Test
    public void setTitlePositiveTest() {
        var c = new CardList("banana");
        c.setTitle("apple");
        assertEquals(c.getTitle(), "apple");
    }

    @Test
    public void setTitleNegativeTest() {
        var c = new CardList("banana");
        c.setTitle("apple");
        assertNotEquals(c.getTitle(), "banana");
    }

    @Test
    public void setCardsPositiveTest() {
        var c = new CardList("banana");
        var t = new Card("apple");

        c.setCards(List.of(t));
        assertEquals(c.getCards(), List.of(t));
    }

    @Test
    public void setCardsNegativeTest() {
        var c = new CardList("banana");
        var t = new Card("apple");

        c.setCards(List.of(t));
        assertNotEquals(c.getCards(), List.of(new Card("banana")));
    }

    @Test
    public void setIdPositiveTest() {
        var c = new CardList("banana");
        c.setId(1);
        assertEquals(c.getId(), 1);
    }

    @Test
    public void setIdNegativeTest() {
        var c = new CardList("banana");
        c.setId(1);
        assertNotEquals(c.getId(), 2);
    }

    @Test
    public void equalsPositiveTest() {
        var c = new CardList("banana");
        var c2 = new CardList("banana");
        assertEquals(c, c2);

        c.setId(1);
        c2.setId(3);
        assertNotEquals(c, c2);
    }

    @Test
    public void equalsNegativeTest() {
        var c = new CardList("banana");
        var c2 = new CardList("apple");
        assertNotEquals(c, c2);
    }

    @Test
    public void hashCodeTest() {
        var c = new CardList("banana");
        var c2 = new CardList("banana");
        assertEquals(c.hashCode(), c2.hashCode());

        var c3 = new CardList("apple");
        c3.setTitle("banana");
        assertEquals(c.hashCode(), c3.hashCode());
    }

    @Test
    public void cloneTest() throws CloneNotSupportedException {
        var c = new CardList("banana");
        c.setCards(List.of(new Card("apple")));
        c.getCards().get(0).getTags().add(new Tag("pear"));

        var c2 = c.clone();
        assertEquals(c, c2);
    }
}
