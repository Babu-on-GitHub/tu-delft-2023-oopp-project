package server.services;

import commons.Board;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class SynchronizationService {
    private final HashSet<Long> boardsToUpdate = new HashSet<>();

    @Autowired
    @Lazy
    private BoardService boardService;

    public void addBoardToUpdate(Long id) {
        boardsToUpdate.add(id);
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
