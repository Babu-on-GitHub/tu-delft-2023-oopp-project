package server.api;

import commons.Board;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import server.database.BoardRepository;

import java.util.List;

@RestController
@RequestMapping("/api/board")
public class BoardController {

    private BoardRepository boardRepository;

    public BoardController(BoardRepository boardRepository){
        this.boardRepository = boardRepository;
    }

    @GetMapping(path = { "", "/" })
    public List<Board> getAll() {
        return boardRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Board> getById(@PathVariable("id") long id) {
        if (id < 0 || !boardRepository.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(boardRepository.findById(id).get());
    }

    @PostMapping(path = "/add")
    public ResponseEntity<Board> add(@RequestBody Board board) {
        if(board == null) {
            return ResponseEntity.badRequest().build();
        }
        Board saved = boardRepository.save(board);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping(path = "/remove/{id}")
    public ResponseEntity<Boolean> remove(@PathVariable("id") long id) {
        if(!boardRepository.existsById(id)){
            return ResponseEntity.badRequest().build();
        }
        boardRepository.deleteById(id);
        return ResponseEntity.ok(true);
    }

    @PutMapping(path = "/update/{id}")
    public ResponseEntity<Board> update(@RequestBody Board board, @PathVariable("id") long id) {
        if(board == null || !boardRepository.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        var saved = boardRepository.save(board);
        return ResponseEntity.ok(saved);
    }

    @MessageMapping("/board/update/{id}")
    @SendTo("/topic/board/{id}")
    public Board updateBoard(Board board, @DestinationVariable long id) {
        return update(board, id).getBody();
    }

    @MessageMapping("/board/remove/{id}")
    @SendTo("/topic/board/{id}")
    public Board removeBoard(@DestinationVariable long id) {
        remove(id);
        return null;
    }
}
