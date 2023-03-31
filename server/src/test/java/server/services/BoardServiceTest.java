package server.services;

import commons.Board;
import commons.Card;
import commons.CardList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.repository.TestBoardRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BoardServiceTest {

    private BoardService boardService;

    @BeforeEach
    public void setup() {
        boardService = new BoardService(new TestBoardRepository(), new TestSynchronizationService());
    }

    @Test
    void findAllBoards() {
        var boards = boardService.findAllBoards();
        assertEquals(0, boards.size());
    }

    @Test
    void getBoardById() {
        // should throw illegal argument
        assertThrows(IllegalArgumentException.class, () -> boardService.getBoardById(1L));
    }

    @Test
    void getExistingBoardById() {
        var board = new Board();
        board.setId(1L);
        boardService.saveBoard(board);
        var boardId = board.getId();
        var foundBoard = boardService.getBoardById(boardId);
        assertEquals(boardId, foundBoard.getId());
    }

    @Test
    void saveBoard() {
        var board = new Board();
        board.setId(1L);
        boardService.saveBoard(board);
        var boards = boardService.findAllBoards();
        assertEquals(1, boards.size());
    }


    @Test
    void saveCardList_invalidBoard() {
        var list = new CardList();
        list.setId(1L);
        try {
            boardService.saveCardList(list, 1L);
        } catch (IllegalArgumentException e) {
            assertEquals("Trying to add list into non-existent board", e.getMessage());
        }
    }

    @Test
    void deleteBoardById() {
        var board = new Board();
        board.setId(1L);
        boardService.saveBoard(board);
        var boardId = board.getId();

        boardService.deleteBoardById(boardId);
        var boards = boardService.findAllBoards();
        assertEquals(0, boards.size());
    }

    @Test
    void deleteBoardById_invalidBoard() {
        assertThrows(IllegalArgumentException.class,
                () -> boardService.deleteBoardById(2L));
    }

    @Test
    void deleteBoardById_defaultBoard() {
        for (int i = 0; i < 5; i++) {
            var board = new Board();
            board.setId((long) i);
            boardService.saveBoard(board);
        }

        try {
            boardService.deleteBoardById(1L);
        } catch (IllegalArgumentException e) {
            assertEquals("Deleting default board is prohibited", e.getMessage());
        }
    }


    @Test
    void deleteCardList_invalidBoard() {
        try {
            boardService.deleteCardListById(1L, 1L);
        } catch (IllegalArgumentException e) {
            assertEquals("Trying to delete list from non-existent board", e.getMessage());
        }
    }


    @Test
    void testEverythingForNull() {
        assertThrows(IllegalArgumentException.class, () -> boardService.saveBoard(null));
        assertThrows(IllegalArgumentException.class, () -> boardService.saveCardList(null, 1L));
        assertThrows(IllegalArgumentException.class, () -> boardService.update(null, 1L));
    }

}
