package server.api;

import commons.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.MessageDeliveryException;
import server.fake.TestSimpMessagingTemplate;
import server.repository.TestBoardRepository;
import server.services.SynchronizationService;
import server.services.TestBoardService;
import server.services.TestSynchronizationService;

import static org.junit.jupiter.api.Assertions.*;

public class WebSocketControllerTest {
    private server.api.WebSocketController webSocketController;
    private SynchronizationService synchronizationService;

    @BeforeEach
    public void setup() {
        synchronizationService = new TestSynchronizationService();
        webSocketController = new server.api.WebSocketController(
                new TestSimpMessagingTemplate(), synchronizationService);
    }

    @Test
    void handlerTest() {
        webSocketController.handler(-1L);

        assertThrows(IllegalArgumentException.class,
                () -> synchronizationService.getBoardsToUpdate().size());
    }

    @Test
    void sendMessageEmpty() {
        // should not throw any exception
        webSocketController.sendMessage();
    }

    @Test
    void sendMessageTest() {
        // should not throw any exception
        var board = new Board("banana");
        synchronizationService.getBoardService().saveBoard(board);
        var boardId = board.getId();
        synchronizationService.addBoardToUpdate(boardId);

        assertThrows(MessageDeliveryException.class, () -> webSocketController. sendMessage());
    }

}
