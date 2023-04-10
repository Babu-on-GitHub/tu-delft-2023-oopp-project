package server.api;

import org.junit.jupiter.api.Test;
import server.LongPollingController;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LongPollingControllerTest {
    @Test
    public void test() {
        var longPollingController = new LongPollingController();
        var result = longPollingController.longPolling();
        assertEquals("OK", result.getResult());
    }
}
