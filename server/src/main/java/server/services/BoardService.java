package server.services;

import commons.Board;
import commons.CardList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.api.BoardController;
import server.database.BoardRepository;

import java.util.List;
import java.util.logging.Logger;

@Service
public class BoardService {
    private static final Logger log = Logger.getLogger(BoardController.class.getName());

    private BoardRepository boardRepository;

    @Autowired
    SynchronizationService synchronizationService;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public List<Board> findAllBoards() {
        return boardRepository.findAll();
    }

    public Board getBoardById(long id) {
        if (id < 0 || boardRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("Invalid board id:" + id);
        }
        var board = boardRepository.findById(id).get();
        return board;
    }

    public Board saveBoard(Board board) {
        if (board == null) {
            throw new IllegalArgumentException("Board cannot be null");
        }

        synchronizationService.addBoardToUpdate(board.getId());
        return boardRepository.save(board);
    }

    public CardList saveCardList(CardList list, long boardId) {
        if (list == null) {
            throw new IllegalArgumentException("Trying to add null list");
        }

        try {
            var boardOptional = this.getBoardById(boardId);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Trying to add list into non-existent board");
        }

        var board = this.getBoardById(boardId);
        list.sync();
        board.getLists().add(list);

        board.sync();
        this.saveBoard(board);
        var ret = board.getLists().get(board.getLists().size() - 1);

        return ret;
    }

    public void deleteBoardById(long id) {
        if (id < 0 || boardRepository.findById(id).orElse(null) == null) {
            throw new IllegalArgumentException("Invalid board id:" + id);
        }

        if (id == 1)
            throw new IllegalArgumentException("Deleting default board is prohibited");

        boardRepository.deleteById(id);
    }

    public void deleteCardListById(long listId, long boardId) {
        try {
            var boardOptional = this.getBoardById(boardId);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Trying to delete list from non-existent board");
        }

        var board = this.getBoardById(boardId);
        var lists = board.getLists();
        for (int i = 0; i < lists.size(); i++) {
            if (lists.get(i).getId() == listId) {
                lists.remove(i);
                board.sync();
                this.saveBoard(board);
                return;
            }
        }
        throw new IllegalArgumentException("Trying to delete non-existent list");
    }

    public void moveCard(long cardId, long listId, int index, long boardId) {
        try {
            var boardOptional = this.getBoardById(boardId);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Trying to move card in non-existent board");
        }

        var board = this.getBoardById(boardId);

        var lists = board.getLists();
        var targetListOpt = lists.stream()
                .filter(list -> list.getId() == listId).findFirst();
        if (targetListOpt.isEmpty()) {
            throw new IllegalArgumentException("Trying to move card to non-existent list");
        }
        var targetList = targetListOpt.get();

        // find the card with correct ID anywhere
        var cardOpt = lists.stream()
                .flatMap(list -> list.getCards().stream())
                .filter(card -> card.getId() == cardId).findFirst();
        if (cardOpt.isEmpty()) {
            throw new IllegalArgumentException("Trying to move non-existent card");
        }

        var card = cardOpt.get();

        // remove the card from the list it is currently in
        var sourceListOpt = lists.stream()
                .filter(list -> list.getCards().contains(card)).findFirst();
        if (sourceListOpt.isEmpty()) {
            throw new IllegalArgumentException("Something went wrong while moving card");
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
        saveBoard(board);
    }

    public Board update(Board board, long id) {
        if (board == null) {
            throw new IllegalArgumentException("Board is null");
        }
        log.info("Updating board: " + board.getId());
        try {
            var boardOptional = this.getBoardById(id);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Trying to update non existing board");
        }

        if (id != board.getId()) {
            throw new IllegalArgumentException("Ids are not coherent in board update");
        }

        try {
            var stored = this.getBoardById(id);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Something went wrong while updating board");
        }

        board.sync();
        return this.saveBoard(board);
    }
}
