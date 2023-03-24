package server.api;

import commons.Board;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import server.services.BoardService;

import java.util.logging.Logger;

@Controller
public class BoardWebsocketController {

    static Logger log = Logger.getLogger(BoardController.class.getName());

    private BoardService boardService;

    public BoardWebsocketController(BoardService boardService) {
        this.boardService = boardService;
    }

    @MessageMapping("/board/update")
    @SendTo("/topic/board")
    public Board sendUpdatedBoard(@Payload Long id){
        System.out.println("banana");
        return boardService.getBoardById(id);
    }

    @Scheduled(fixedRate = 1000)
    @SendTo("/topic/board")
    public Board sendRegularly(){
        System.out.println("banana banana");
        return boardService.getBoardById(1);
    }

}
