package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {

    @Test
    public void constructorTest() {
        var t = new Task("banana");
        assertEquals(t.getTitle(), "banana");
    }

    @Test
    public void constructorTest2() {
        var t = new Task(1, "pear");
        assertEquals(t.getTitle(), "pear");
        assertEquals(t.getId(), 1);
    }

    @Test
    public void defaultConstructorTest() {
        var t = new Task();
        assertEquals(t.getTitle(),"Untitled task");
        assertEquals(t.getId(), 0);
    }

    @Test
    public void setTitlePositiveTest() {
        var t = new Task("banana");
        t.setTitle("apple");
        assertEquals(t.getTitle(), "apple");
    }

    @Test
    public void setTitleNegativeTest() {
        var t = new Task("banana");
        t.setTitle("apple");
        assertNotEquals(t.getTitle(), "banana");
    }

    @Test
    public void setIdPositiveTest() {
        var t = new Task("banana");
        t.setId(1);
        assertEquals(t.getId(), 1);
    }

    @Test
    public void setIdNegativeTest() {
        var t = new Task("banana");
        t.setId(1);
        assertNotEquals(t.getId(), 2);
    }

    @Test
    public void equalsPositiveTest() {
        var t = new Task("banana");
        var t2 = new Task("banana");
        assertEquals(t, t2);

        var t3 = new Task("apple");
        t3.setTitle("banana");

        assertEquals(t, t3);
    }

    @Test
    public void equalsNegativeTest() {
        var t = new Task("banana");
        var t2 = new Task("apple");
        assertNotEquals(t, t2);

        t2.setTitle("banana");
        assertEquals(t, t2);
    }

    @Test
    public void hashCodeTest() {
        var t = new Task("banana");
        var t2 = new Task("banana");
        assertEquals(t.hashCode(), t2.hashCode());

        var t3 = new Task("apple");
        t3.setTitle("banana");
        assertEquals(t.hashCode(), t3.hashCode());
    }

    @Test
    public void checkedTest() {
        var t = new Task("mango");
        assertFalse(t.isChecked());
        t.setChecked(true);
        assertTrue(t.isChecked());
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        var t = new Task("mango");
        var t2 = t.clone();
        assertEquals(t, t2);
    }
}
