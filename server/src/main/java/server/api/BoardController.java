package server.api;

import commons.Board;
import commons.CardList;
import org.springframework.http.ResponseEntity;
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

    @PostMapping(path = { "", "/" })
    public ResponseEntity<Board> add(@RequestBody Board board) {
        if(board == null){
            return ResponseEntity.badRequest().build();
        }
        Board saved = boardRepository.save(board);
        return ResponseEntity.ok(saved);
    }
}
