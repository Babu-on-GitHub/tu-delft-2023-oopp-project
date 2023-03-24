package server.api;

import commons.Board;
import commons.CardList;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.BoardRepository;
import server.services.BoardService;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/board")
public class BoardController {

    static Logger log = Logger.getLogger(BoardController.class.getName());

    private BoardService boardService;

    public BoardController(BoardService boardService){
        this.boardService = boardService;
    }

    @GetMapping(path = { "", "/" })
    public List<Board> getAll() {
        return boardService.findAllBoards();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Board> getById(@PathVariable("id") long id) {
        log.info("Getting board: " + id);
        if (id < 0 || boardService.getBoardById(id) == null) {
            log.warning("Trying to get non existing board");
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(boardService.getBoardById(id));
    }

    @PostMapping(path = "/create")
    public ResponseEntity<Board> create(@RequestBody Board board) {
        if (board == null) {
            log.warning("Trying to create null board");
            return ResponseEntity.badRequest().build();
        }

        board.sync();
        var saved = boardService.saveBoard(board);

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

        var boardOptional = boardService.getBoardById(boardId);
        if (boardOptional == null) {
            log.warning("Trying to add list into non-existent board");
            return ResponseEntity.badRequest().build();
        }

        var board = boardService.getBoardById(boardId);

        list.sync();
        board.getLists().add(list);

        board.sync();
        boardService.saveBoard(board);

        // extract the newly assigned id from the db
        var ret = board.getLists().get(board.getLists().size() - 1);

        log.info("New list id: " + ret.getId());

        return ResponseEntity.ok(ret);
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable("id") long id) {
        log.info("Deleting board: " + id);
        if (id < 0 || boardService.getBoardById(id) == null) {
            log.warning("Trying to delete non existing board");
            return ResponseEntity.badRequest().build();
        }
        boardService.deleteBoardById(id);
        return ResponseEntity.ok(true);
    }

    @DeleteMapping(path = "/delete/{listId}/from/{boardId}")
    public ResponseEntity<Boolean> remove(@PathVariable("listId") long listId, @PathVariable("boardId") long boardId) {
        log.info("Deleting board: " + listId + " from board: " + boardId);

        var boardOptional = boardService.getBoardById(boardId);
        if (boardOptional == null) {
            log.warning("Trying to delete list from non-existent board");
            return ResponseEntity.badRequest().build();
        }

        var board = boardService.getBoardById(boardId);
        var lists = board.getLists();
        for (int i = 0; i < lists.size(); i++) {
            if (lists.get(i).getId() == listId) {
                lists.remove(i);
                board.sync();
                boardService.saveBoard(board);
                return ResponseEntity.ok(true);
            }
        }

        log.warning("Trying to delete non-existent list");

        return ResponseEntity.ok(false);
    }

    @PostMapping(path = "moveCard/{cardId}/to/{listId}/at/{index}/located/{boardId}")
    public ResponseEntity<Boolean> moveCard(@PathVariable("cardId") long cardId,
                                            @PathVariable("listId") long listId,
                                            @PathVariable("index") int index,
                                            @PathVariable("boardId") long boardId) {
        log.info("Moving card: " + cardId + " to list: " + listId +
                " at index: " + index + " located in board: " + boardId);

        var boardOptional = boardRepository.findById(boardId);
        if (boardOptional.isEmpty()) {
            log.warning("Trying to move card in non-existent board");
            return ResponseEntity.badRequest().build();
        }
        var board = boardOptional.get();

        var lists = board.getLists();
        var targetListOpt = lists.stream()
                .filter(list -> list.getId() == listId).findFirst();
        if (targetListOpt.isEmpty()) {
            log.warning("Trying to move card to non-existent list");
            return ResponseEntity.badRequest().build();
        }
        var targetList = targetListOpt.get();

        // find the card with correct ID anywhere
        var cardOpt = lists.stream()
                .flatMap(list -> list.getCards().stream())
                .filter(card -> card.getId() == cardId).findFirst();
        if (cardOpt.isEmpty()) {
            log.warning("Trying to move non-existent card");
            return ResponseEntity.badRequest().build();
        }

        var card = cardOpt.get();

        // remove the card from the list it is currently in
        var sourceListOpt = lists.stream()
                .filter(list -> list.getCards().contains(card)).findFirst();
        if (sourceListOpt.isEmpty()) {
            log.warning("Something went wrong while moving card");
            return ResponseEntity.badRequest().build();
        }

        var sourceList = sourceListOpt.get();
        sourceList.getCards().remove(card);

        if (index > targetList.getCards().size()) {
            log.info("Index out of bounds, moving to the end of list");
            index = targetList.getCards().size();
        }

        targetList.getCards().add(index, card);
        board.sync();
        sourceList.sync();
        targetList.sync();
        boardRepository.save(board);

        return ResponseEntity.ok(true);
    }

    @PutMapping(path = "/update/{id}")
    public ResponseEntity<Board> update(@RequestBody Board board, @PathVariable("id") long id) {
        if (board == null) {
            return ResponseEntity.badRequest().build();
        }

        log.info("Updating board: " + board.getId());

        if (boardService.getBoardById(id) == null) {
            log.warning("Trying to update non existing board");
            return ResponseEntity.badRequest().build();
        }

        if (id != board.getId())
            log.warning("Ids are not coherent in board update");

        var stored = boardService.getBoardById(id);
        if (stored == null) {
            log.warning("Something went wrong while updating board");
            return ResponseEntity.badRequest().build();
        }

        board.sync();
        var saved = boardService.saveBoard(board);
        return ResponseEntity.ok(saved);
    }
}
