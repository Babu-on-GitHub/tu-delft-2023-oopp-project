package server.services;

import commons.Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SynchronizationService {
    protected final HashSet<Long> boardsToUpdate = new HashSet<>();

    @Lazy
    protected BoardService boardService;

    public SynchronizationService() {
    }
    public void addBoardToUpdate(Long id) {
        boardsToUpdate.add(id);
    }

    public BoardService getBoardService() {
        return boardService;
    }

    @Autowired
    public SynchronizationService(@Lazy BoardService boardService) {
        this.boardService = boardService;
    }

    public void addListToUpdate(Long id) {
        var boards = boardService.findAllBoards();
        boards.stream()
                .filter(board -> board.getLists().stream()
                        .anyMatch(list -> list.getId() == id))
                .forEach(board -> boardsToUpdate.add(board.getId()));
    }

    public void addCardToUpdate(Long id) {
        var boards = boardService.findAllBoards();
        boards.stream()
                .filter(board -> board.getLists().stream()
                        .anyMatch(list -> list.getCards().stream()
                                .anyMatch(card -> card.getId() == id)))
                .forEach(board -> boardsToUpdate.add(board.getId()));
    }

    public List<Board> getBoardsToUpdate() {
        var res = new ArrayList<Board>();

        for (var id : boardsToUpdate) {
            var board = boardService.getBoardById(id);
            res.add(board);
        }

        return res;
    }

    public void clearBoardsToUpdate() {
        boardsToUpdate.clear();
    }
}
