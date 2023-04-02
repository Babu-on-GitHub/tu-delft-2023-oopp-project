package server.services;

import commons.Board;
import commons.Card;
import commons.CardList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.repository.TestBoardRepository;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SynchronizationServiceTest {
    private SynchronizationService synchronizationService;

    @BeforeEach
    public void setup() {
        synchronizationService = new SynchronizationService(
                new TestBoardService(new TestBoardRepository(), new TestSynchronizationService())
        );
    }

    @Test
    void addBoardTest() {
        synchronizationService.addBoardToUpdate(1L);
        // should throw illegal argument
        assertThrows(IllegalArgumentException.class, () ->
                synchronizationService.getBoardsToUpdate());

    }

    @Test
    void addCardTest() {
        var board = new Board("banana");
        var list = new CardList("xxx");
        var card = new Card("lolz");
        board.setId(1L);
        list.add(card);
        board.add(list);

        synchronizationService.getBoardService().saveBoard(board);
        var boardId = board.getId();
        var cardId = card.getId();

        synchronizationService.addCardToUpdate(cardId);

        var boards = synchronizationService.getBoardsToUpdate();

        // boards should contain a board with id boardId
        var boardFound = boards.stream().anyMatch(b -> b.getId() == boardId);
        assertTrue(boardFound);
    }

    @Test
    void addCardListTest() {
        var board = new Board("banana");
        board.setId(1L);
        var list = new CardList("xxx");
        board.add(list);

        synchronizationService.getBoardService().saveBoard(board);
        var boardId = board.getId();
        var listId = list.getId();

        synchronizationService.addListToUpdate(listId);

        var boards = synchronizationService.getBoardsToUpdate();
        // boards should contain a board with id boardId
        var boardFound = boards.stream().anyMatch(b -> b.getId() == boardId);
        assertTrue(boardFound);

    }
}
