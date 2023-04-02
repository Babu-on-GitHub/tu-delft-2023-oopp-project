package client.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SocketUtilsTest {

    private TestSocketUtils socketUtils;

    @BeforeEach
    public void setUp() {
        socketUtils = new TestSocketUtils();
    }

    @Test
    void socketUtilsConnectDisconnectTest() {
        socketUtils.setServer("localhost:8080");
        // connect should throw runtime exception
        assertThrows(RuntimeException.class, () -> socketUtils.connect());

        // disconnect should not throw exception
        socketUtils.disconnect();
    }

    @Test
    void socketUtilsRegisterTest() {
        socketUtils.setServer("localhost:8080");
        // register should throw null pointer exception, since there is no session
        assertThrows(NullPointerException.class,
                () -> socketUtils.registerForMessages("test", String.class, (s) -> {}));
    }

    @Test
    void sendTest() {
        socketUtils.setServer("localhost:8080");
        // send should throw null pointer exception, since there is no session
        assertThrows(NullPointerException.class, () -> socketUtils.send("test", "test"));
    }

    @Test
    void wrapWithWebsocketTest() {
        assertEquals("ws://test/websocket",
                socketUtils.wrapWithWebsocket("test"));
        assertEquals("ws://test.com/websocket",
                socketUtils.wrapWithWebsocket("http://test.com"));
        assertEquals("wss://test/websocket",
                socketUtils.wrapWithWebsocket("https://test"));
        assertEquals("ws://test/websocket",
                socketUtils.wrapWithWebsocket("ws://test/websocket"));
    }
}
