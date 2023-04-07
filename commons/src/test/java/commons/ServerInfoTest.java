package commons;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ServerInfoTest {

    @Test
    void constructorTest() {
        var s = new ServerInfo("pear");
        assertNotNull(s);
        assertNotNull(s.getBoardsIds());
        assertEquals("pear", s.getServerAddress());
    }

    @Test
    void defaultConstructorTest() {
        var s = new ServerInfo();
        assertNotNull(s.getBoardsIds());
    }

    @Test
    void setServerAddress() {
        var s = new ServerInfo();
        s.setServerAddress("orange");
        assertEquals(s.getServerAddress(), "orange");
    }

    @Test
    void setBoardIds() {
        var s = new ServerInfo();
        var b = List.of(new BoardIdWithColors());
        s.setBoardsIds(b);
        assertEquals(s.getBoardsIds(), b);
    }

    @Test
    void equalsTest() {
        var s = new ServerInfo("mango");
        var s2 = new ServerInfo("mango");
        assertEquals(s, s2);

        var s3 = new ServerInfo("banana");
        assertNotEquals(s, s3);
        s3.setServerAddress("mango");
        assertEquals(s, s3);
    }
}
