package server.api;

import commons.Board;
import commons.CardList;
import commons.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.services.BoardService;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/board")
public class BoardController {

    static Logger log = Logger.getLogger(BoardController.class.getName());

    private BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping(path = {"", "/"})
    public List<Board> getAll() {
        return boardService.findAllBoards();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Board> getById(@PathVariable("id") long id) {
        try {
            log.info("Getting board: " + id);
            Board board = boardService.getBoardById(id);
            return ResponseEntity.ok(board);
        } catch (IllegalArgumentException e) {
            log.warning(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }


    @PostMapping(path = "/create")
    public ResponseEntity<Board> create(@RequestBody Board board) {
        try {
            board.sync();
            var saved = boardService.saveBoard(board);
            log.info("Created board: " + saved.getId());
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException e) {
            log.warning(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }


    @PostMapping(path = "/add/{id}")
    public ResponseEntity<CardList> add(@RequestBody CardList list, @PathVariable("id") long boardId) {

        log.info("Adding list: " + list.getId() + " to board: " + boardId);
        try {
            var ret = boardService.saveCardList(list, boardId);
            if (ret!=null){
                log.info("New list id: " + ret.getId());
                return ResponseEntity.ok(ret);
            }
            return ResponseEntity.badRequest().build();
        } catch (IllegalArgumentException e) {
            log.warning(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable("id") long id) {
        log.info("Deleting board: " + id);

        try {
            boardService.deleteBoardById(id);
            return ResponseEntity.ok(true);
        } catch (IllegalArgumentException e) {
            log.warning(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping(path = "/delete/{listId}/from/{boardId}")
    public ResponseEntity<Boolean> remove(@PathVariable("listId") long listId, @PathVariable("boardId") long boardId) {
        log.info("Deleting list: " + listId + " from board: " + boardId);
        try {
            boardService.deleteCardListById(listId, boardId);
            return ResponseEntity.ok(true);
        } catch (IllegalArgumentException e) {
            log.warning(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(path = "moveCard/{cardId}/to/{listId}/at/{index}/located/{boardId}")
    public ResponseEntity<Boolean> moveCard(@PathVariable("cardId") long cardId,
                                            @PathVariable("listId") long listId,
                                            @PathVariable("index") int index,
                                            @PathVariable("boardId") long boardId) {
        log.info("Moving card: " + cardId + " to list: " + listId +
                " at index: " + index + " located in board: " + boardId);
        try {
            boardService.moveCard(cardId, listId, index, boardId);
            return ResponseEntity.ok(true);
        } catch (IllegalArgumentException e) {
            log.warning(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(path = "/update/{id}")
    public ResponseEntity<Board> update(@RequestBody Board board, @PathVariable("id") long id) {
        log.info("Updating board: " + id);
        try {
            var saved = boardService.update(board, id);
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException e) {
            log.warning(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(path = "/updateTitle/{id}")
    public ResponseEntity<String> updateTitle(@RequestBody String title, @PathVariable("id") long id) {
        log.info("Updating title of board: " + id);
        try {
            var saved = boardService.updateTitle(title, id);
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException e) {
            log.warning(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(path = "/addTag/{boardId}")
    public ResponseEntity<Tag> addTag(@RequestBody Tag tag, @PathVariable("boardId") long boardId) {
        log.info("Adding tag: " + tag.getId() + " to board: " + boardId);
        try {
            var saved = boardService.addTag(tag, boardId);
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException e) {
            log.warning(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping(path = "/deleteTag/{tagId}/from/{boardId}")
    public ResponseEntity<Boolean> deleteTag(@PathVariable("boardId") long boardId, @PathVariable("tagId") long tagId) {
        log.info("Deleting tag: " + tagId + " from board: " + boardId);
        try {
            boardService.deleteTag(tagId, boardId);
            return ResponseEntity.ok(true);
        } catch (IllegalArgumentException e) {
            log.warning(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(path = "/getTags/{boardId}")
    public ResponseEntity<Set<Tag>> getTags(@PathVariable("boardId") long id) {
        log.info("Getting tags from board: " + id);
        try {
            var tags = boardService.getTags(id);
            return ResponseEntity.ok(tags);
        } catch (IllegalArgumentException e) {
            log.warning(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(path = "/updateTag/{boardId}")
    public ResponseEntity<Tag> updateTag(@RequestBody Tag tag, @PathVariable("boardId") long boardId) {
        log.info("Updating tag: " + tag.getId() + " from board: " + boardId);
        try {
            var saved = boardService.updateTag(boardId, tag);
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException e) {
            log.warning(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
