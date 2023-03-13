package commons;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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
    public void setSubtasksPositiveTest() {
        var c = new Card("banana");
        var t = new Task();
        t.setTitle("apple");

        c.setSubTasks(List.of(t));
        assertEquals(c.getSubTasks(), List.of(t));
    }

    @Test
    public void setSubtasksNegativeTest() {
        var c = new Card("banana");
        var t = new Task();
        t.setTitle("apple");

        c.setSubTasks(List.of(t));
        assertNotEquals(c.getSubTasks(), new ArrayList<Task>());

        var notT = new Task();
        notT.setTitle("banana");
        assertNotEquals(c.getSubTasks(), List.of(notT));
    }

    @Test
    public void setTagsPositiveTest() {
        var c = new Card("banana");
        var t = new Tag();
        t.setTitle("apple");

        c.setTags(Set.of(t));
        assertEquals(c.getTags(), Set.of(t));
    }

    @Test
    public void setTagsNegativeTest() {
        var c = new Card("banana");
        var t = new Tag();
        t.setTitle("apple");

        c.setTags(Set.of(t));
        assertNotEquals(c.getTags(), new HashSet<Tag>());

        var notT = new Tag();
        notT.setTitle("banana");
        assertNotEquals(c.getTags(), Set.of(notT));
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
    public void equalsTasksPositiveTest() {
        var c = new Card("banana");
        var t = new Task();
        t.setTitle("apple");

        c.setSubTasks(List.of(t));
        var c2 = new Card("banana");
        c2.setSubTasks(List.of(t));
        assertEquals(c, c2);
    }

    @Test
    public void equalsTasksNegativeTest() {
        var c = new Card("banana");
        var t = new Task();
        t.setTitle("apple");

        c.setSubTasks(List.of(t));
        var c2 = new Card("banana");

        var notT = new Task();
        notT.setTitle("orange");
        c2.setSubTasks(List.of(notT));
        assertNotEquals(c, c2);
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
}
