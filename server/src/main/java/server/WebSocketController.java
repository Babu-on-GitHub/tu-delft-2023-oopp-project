package server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate template;

    @Scheduled(fixedRate = 1000)
    @MessageMapping("")
    @SendTo("/topic")
    public void message() {
        String message = "This is some info from server";
        template.convertAndSend("/topic", message);
    }
}
