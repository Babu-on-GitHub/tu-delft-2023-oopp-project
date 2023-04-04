package client.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static junit.framework.TestCase.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestLongPollingUtils {

    LongPollingUtils utils;

    @BeforeEach
    public void setUp() {
        utils = new LongPollingUtils();
        utils.setServer("asdf");
    }
    @Test
    void wrapWithLongPollingProtocolTest() {
        assertEquals("http://test/long",
                LongPollingUtils.wrapWithLongPollingProtocol("test"));
    }
    @Test
    void wrapWithLongPollingProtocolTest2() {
        assertEquals("http://test/long",
                LongPollingUtils.wrapWithLongPollingProtocol("https://test"));
    }
    @Test
    void wrapWithLongPollingProtocolTest3() {
        assertEquals("http://test/long",
                LongPollingUtils.wrapWithLongPollingProtocol("http://test"));
    }

    @Test
    void longPollingTest() throws InterruptedException {
        AtomicBoolean wasCalled = new AtomicBoolean(false);
        utils.longPoll("test", (s) -> {
            Assertions.assertTrue(s.isEmpty());
            wasCalled.set(true);
        });
        Thread.sleep(1000L);
        utils.stopLongPolling();

        assertTrue(wasCalled.get());
    }
}
