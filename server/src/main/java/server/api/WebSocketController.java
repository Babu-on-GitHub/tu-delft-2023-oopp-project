package server.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import server.services.SynchronizationService;

import java.util.logging.Logger;

@Controller
@EnableScheduling
public class WebSocketController {
    private static final Logger log = Logger.getLogger(WebSocketController.class.getName());

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private SynchronizationService synchronizationService;

    // this message mapping needed "just in case" if we want to force an update of a board
    @MessageMapping("/update")
    public void f(@Payload Long id) throws Exception {
        log.info("Received update request for board: " + id);
        synchronizationService.addBoardToUpdate((long) id);
    }

    @Scheduled(fixedRate = 200)
    public void sendMessage() {
        if (synchronizationService.getBoardsToUpdate().isEmpty()) {
            return;
        }


        for (var board : synchronizationService.getBoardsToUpdate()) {
            var index = board.getId();
            log.info("Sending board: " + index + " to all subscribers");
            simpMessagingTemplate.convertAndSend("/topic/board/" + index,
                    board);
        }

        synchronizationService.clearBoardsToUpdate();
    }
}
