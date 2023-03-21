package server.api;

import commons.Board;
import commons.CardList;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.BoardRepository;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/board")
public class BoardController {

    static Logger log = Logger.getLogger(BoardController.class.getName());

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
        log.info("Getting board: " + id);
        if (id < 0 || !boardRepository.existsById(id)) {
            log.warning("Trying to get non existing board");
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(boardRepository.findById(id).get());
    }

    @PostMapping(path = "/create")
    public ResponseEntity<Board> create(@RequestBody Board board) {
        if (board == null) {
            log.warning("Trying to create null board");
            return ResponseEntity.badRequest().build();
        }

        board.sync();
        var saved = boardRepository.save(board);

        log.info("Created board: " + saved.getId());

        return ResponseEntity.ok(saved);
    }

    @PostMapping(path = "/add/{id}")
    public ResponseEntity<CardList> add(@RequestBody CardList list, @PathVariable("id") long boardId) {
        if (list == null) {
            log.warning("Trying to add null list");
            return ResponseEntity.badRequest().build();
        }

        log.info("Adding list: " + list.getId() + " to board: " + boardId);

        var boardOptional = boardRepository.findById(boardId);
        if (boardOptional.isEmpty()) {
            log.warning("Trying to add list into non-existent board");
            return ResponseEntity.badRequest().build();
        }

        var board = boardOptional.get();

        list.sync();
        board.getLists().add(list);

        board.sync();
        boardRepository.save(board);

        // extract the newly assigned id from the db
        var ret = board.getLists().get(board.getLists().size() - 1);

        log.info("New list id: " + ret.getId());

        return ResponseEntity.ok(ret);
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable("id") long id) {
        log.info("Deleting board: " + id);
        if (id < 0 || !boardRepository.existsById(id)) {
            log.warning("Trying to delete non existing board");
            return ResponseEntity.badRequest().build();
        }
        boardRepository.deleteById(id);
        return ResponseEntity.ok(true);
    }

    @DeleteMapping(path = "/delete/{listId}/from/{boardId}")
    public ResponseEntity<Boolean> remove(@PathVariable("listId") long listId, @PathVariable("boardId") long boardId) {
        log.info("Deleting board: " + listId + " from board: " + boardId);

        var boardOptional = boardRepository.findById(boardId);
        if (boardOptional.isEmpty()) {
            log.warning("Trying to delete list from non-existent board");
            return ResponseEntity.badRequest().build();
        }

        var board = boardOptional.get();
        var lists = board.getLists();
        for (int i = 0; i < lists.size(); i++) {
            if (lists.get(i).getId() == listId) {
                lists.remove(i);
                board.sync();
                boardRepository.save(board);
                return ResponseEntity.ok(true);
            }
        }

        log.warning("Trying to delete non-existent list");

        return ResponseEntity.ok(false);
    }

    @PutMapping(path = "/update/{id}")
    public ResponseEntity<Board> update(@RequestBody Board board, @PathVariable("id") long id) {
        if (board == null) {
            return ResponseEntity.badRequest().build();
        }

        log.info("Updating board: " + board.getId());

        if (!boardRepository.existsById(id)) {
            log.warning("Trying to update non existing board");
            return ResponseEntity.badRequest().build();
        }

        if (id != board.getId())
            log.warning("Ids are not coherent in board update");

        var stored = boardRepository.findById(id);
        if (stored.isEmpty()) {
            log.warning("Something went wrong while updating board");
            return ResponseEntity.badRequest().build();
        }
        var storedBoard = stored.get();
        storedBoard.assign(board);
        storedBoard.sync();

        var saved = boardRepository.save(storedBoard);
        return ResponseEntity.ok(saved);
    }
}
