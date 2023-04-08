package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    void testGetServerBoardsPairs() {
        final List<ServerInfo> expectedResult = new ArrayList<>();
        final List<ServerInfo> result = user.getServerBoardsPairs();
        assertEquals(expectedResult, result);
    }

    @Test
    void testSetServerBoardsPairs() {
        final List<ServerInfo> serverBoardsPairs = new ArrayList<>();
        user.setServerBoardsPairs(serverBoardsPairs);
        assertEquals(serverBoardsPairs, user.getServerBoardsPairs());
    }

    @Test
    void testSaveAndLoad() throws IOException, ClassNotFoundException {
        final List<ServerInfo> serverBoardsPairs = new ArrayList<>();
        serverBoardsPairs.add(new ServerInfo("http://localhost:8080"));
        serverBoardsPairs.add(
                new ServerInfo("http://localhost:8081",
                        List.of(new BoardIdWithColors(1L),
                                new BoardIdWithColors(2L),
                                new BoardIdWithColors(3L)))
        );

        user.setServerBoardsPairs(serverBoardsPairs);

        File file = new File("user.json");
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));

        user.save(oos);
        final User result = User.load(ois);
        oos.close();
        ois.close();

        // remove the file, since now it is irrelevant
        if (!file.delete())
            throw new IOException("Failed to delete file");

        assertEquals(user, result);
    }

    @Test
    void testEqualsWithNull() {
        final User other = null;
        final boolean result = user.equals(other);
        assertFalse(result);
    }

    @Test
    void testConstructorWithBoards() {
        final List<ServerInfo> servers = new ArrayList<>();
        servers.add(new ServerInfo("http://localhost:8080"));
        servers.add(
                new ServerInfo("http://localhost:8081",
                        List.of(new BoardIdWithColors(1L),
                                new BoardIdWithColors(2L),
                                new BoardIdWithColors(3L)))
        );

        final User result = new User(servers);
        assertEquals(servers, result.getServerBoardsPairs());
    }

    @Test
    void getUserBoardIdsTest() {
        final List<ServerInfo> servers = new ArrayList<>();
        servers.add(new ServerInfo("http://localhost:8080",
                List.of(new BoardIdWithColors(1L))));
        servers.add(
                new ServerInfo("http://localhost:8081",
                        List.of(new BoardIdWithColors(1L),
                                new BoardIdWithColors(2L),
                                new BoardIdWithColors(3L)))
        );
        user.setServerBoardsPairs(servers);

        final List<BoardIdWithColors> result = user.getUserBoardsIds("http://localhost:8080");
        assertEquals(List.of(new BoardIdWithColors(1L)), result);

        final List<BoardIdWithColors> result2 = user.getUserBoardsIds("http://localhost:8081");
        assertEquals(List.of(new BoardIdWithColors(1L),
                new BoardIdWithColors(2L),
                new BoardIdWithColors(3L)), result2);
    }

    @Test
    void setUserBoardsForServerTest() {
        final List<ServerInfo> servers = new ArrayList<>();
        servers.add(new ServerInfo("http://localhost:8080"));
        servers.add(
                new ServerInfo("http://localhost:8081",
                        List.of(new BoardIdWithColors(1L),
                        new BoardIdWithColors(2L),
                        new BoardIdWithColors(3L)))
        );
        user.setServerBoardsPairs(servers);

        user.setUserBoardsForServer("http://localhost:8080",
                List.of(new BoardIdWithColors(1L),
                        new BoardIdWithColors(2L),
                        new BoardIdWithColors(3L)));
        final List<BoardIdWithColors> result = user.getUserBoardsIds("http://localhost:8080");
        assertEquals(List.of(new BoardIdWithColors(1L),
                new BoardIdWithColors(2L),
                new BoardIdWithColors(3L)), result);

        user.setUserBoardsForServer("http://localhost:8081",
                List.of(new BoardIdWithColors(1L),
                        new BoardIdWithColors(2L),
                        new BoardIdWithColors(3L)));
        final List<BoardIdWithColors> result2 = user.getUserBoardsIds("http://localhost:8081");
        assertEquals(List.of(new BoardIdWithColors(1L),
                new BoardIdWithColors(2L),
                new BoardIdWithColors(3L)), result2);
    }

    @Test
    void updateSingleBoardForServerTest() {
        user.updateSingleBoardForServer("http://localhost:8080", new BoardIdWithColors(1L));
        List<BoardIdWithColors> result = user.getUserBoardsIds("http://localhost:8080");
        assertEquals(List.of(new BoardIdWithColors(1L)), result);

        user.updateSingleBoardForServer("http://localhost:8081", new BoardIdWithColors(1L));
        List<BoardIdWithColors> result2 = user.getUserBoardsIds("http://localhost:8081");
        assertEquals(List.of(new BoardIdWithColors(1L)), result2);
    }

    @Test
    void makeDefaultTest() {
        assertEquals(new BoardIdWithColors(1L), user.getUserBoardsIds("banana").get(0));
    }
}
