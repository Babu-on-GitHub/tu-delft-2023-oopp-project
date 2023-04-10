package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TagTest {

    @Test
    public void constructorTest() {
        var t = new Tag("banana");
        assertEquals(t.getTitle(), "banana");
    }

    @Test
    public void constructorTest2() {
        var t = new Tag(1, "pear");
        assertEquals(t.getTitle(), "pear");
        assertEquals(t.getId(), 1);
    }

    @Test
    public void defaultConstructorTest() {
        var t = new Tag();
        assertNull(t.getTitle());
        assertEquals(t.getId(), 0);
    }

    @Test
    public void setTitlePositiveTest() {
        var t = new Tag("banana");
        t.setTitle("apple");
        assertEquals(t.getTitle(), "apple");
    }

    @Test
    public void setTitleNegativeTest() {
        var t = new Tag("banana");
        t.setTitle("apple");
        assertNotEquals(t.getTitle(), "banana");
    }

    @Test
    public void setIdPositiveTest() {
        var t = new Tag("banana");
        t.setId(1);
        assertEquals(t.getId(), 1);
    }

    @Test
    public void setIdNegativeTest() {
        var t = new Tag("banana");
        t.setId(1);
        assertNotEquals(t.getId(), 2);
    }

    @Test
    public void setColorTest(){
        var t = new Tag("banana");
        assertNotEquals(t.getColorPair(), new ColorPair("#121212", "343434"));
        var colors = new ColorPair("#121212", "343434");
        t.setColorPair(colors);
        assertEquals(t.getColorPair(), colors);
    }

    @Test
    public void equalsPositiveTest() {
        var t = new Tag("banana");
        var t2 = new Tag("banana");
        assertEquals(t, t2);

        var t3 = new Tag("apple");
        t3.setTitle("banana");
        assertEquals(t, t3);

        var colors = new ColorPair();
        t.setColorPair(colors);
        assertEquals(t,t2);
        t2.setColorPair(colors);
        assertEquals(t,t2);
        t3.setColorPair(new ColorPair("#544444","#333333"));
        assertNotEquals(t,t3);
    }

    @Test
    public void equalsNegativeTest() {
        var t = new Tag("banana");
        var t2 = new Tag("apple");
        assertNotEquals(t, t2);

        var t3 = new Tag("banana");
        t3.setId(3);
        assertNotEquals(t, t3);
    }

    @Test
    public void hashCodeTest() {
        var t = new Tag("banana");
        var t2 = new Tag("banana");
        assertEquals(t.hashCode(), t2.hashCode());

        var t3 = new Tag("apple");
        t3.setTitle("banana");
        assertEquals(t.hashCode(), t3.hashCode());

        var colors = new ColorPair();
        t.setColorPair(colors);
        assertEquals(t.hashCode(),t2.hashCode());
        t2.setColorPair(colors);
        assertEquals(t.hashCode(),t2.hashCode());
        t3.setColorPair(new ColorPair("#bbbbbb", "#aaaaaa"));
        assertNotEquals(t.hashCode(),t3.hashCode());
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        var t = new Tag("banana");
        var t2 = t.clone();
        assertEquals(t, t2);
    }
}
