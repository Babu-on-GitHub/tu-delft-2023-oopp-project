package client.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertTrue;
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
        assertEquals("http://test",
                LongPollingUtils.wrapWithLongPollingProtocol("test"));
    }
    @Test
    void wrapWithLongPollingProtocolTest2() {
        assertEquals("http://test",
                LongPollingUtils.wrapWithLongPollingProtocol("https://test"));
    }
    @Test
    void wrapWithLongPollingProtocolTest3() {
        assertEquals("http://test",
                LongPollingUtils.wrapWithLongPollingProtocol("http://test"));
    }

    @Test
    void longPollingTest() throws InterruptedException {
        AtomicBoolean wasCalled = new AtomicBoolean(false);
        utils.longPoll("test", (s) -> {
            Assertions.assertTrue(s.isEmpty());
            wasCalled.set(true);
        });
        Thread.sleep(10000L);
        utils.stopLongPolling();

        assertTrue(wasCalled.get());
    }


    @Test
    void longPollingTestCard() throws InterruptedException {
        AtomicBoolean wasCalled = new AtomicBoolean(false);
        utils.longPollCard("test", (s) -> {
            Assertions.assertTrue(s == null);
            wasCalled.set(true);
        });
        Thread.sleep(10000L);
        utils.stopCardPolling();

        assertTrue(wasCalled.get());
    }

    @Test
    void longPollingTestId() throws InterruptedException {
        AtomicBoolean wasCalled = new AtomicBoolean(false);
        utils.longPollId("test", (s) -> {
            Assertions.assertTrue(s == null);
            wasCalled.set(true);
        });
        Thread.sleep(10000L);
        utils.stopIdPolling();

        assertTrue(wasCalled.get());
    }
}
