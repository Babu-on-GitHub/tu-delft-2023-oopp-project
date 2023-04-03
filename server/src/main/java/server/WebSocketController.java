package server.api;

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

    private SimpMessagingTemplate simpMessagingTemplate;
    private SynchronizationService synchronizationService;

    public WebSocketController(SimpMessagingTemplate simpMessagingTemplate,
                               SynchronizationService synchronizationService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.synchronizationService = synchronizationService;
    }

    // this message mapping needed "just in case" if we want to force an update of a board
    @MessageMapping("/update")
    public void handler(@Payload Long id) {
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
            log.info("There are " + board.getTags().size() + " tags");

            // print all the tags
            for (var tag : board.getTags()) {
                log.info(tag.toString());
            }

            simpMessagingTemplate.convertAndSend("/topic/board/" + index,
                    board);
        }

        synchronizationService.clearBoardsToUpdate();
    }
}
