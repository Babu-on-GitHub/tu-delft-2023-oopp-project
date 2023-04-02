package server.services;

import commons.Board;
import commons.Card;
import commons.CardList;
import commons.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.repository.TestBoardRepository;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

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
    void saveCardList() {
        var board = new Board();
        board.setId(1L);
        boardService.saveBoard(board);
        var boardId = board.getId();
        var list = new CardList();
        list.setId(1L);
        boardService.saveCardList(list, boardId);
        var boards = boardService.findAllBoards();
        assertEquals(1, boards.size());
        assertEquals(1, boards.get(0).getLists().size());
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
    void deleteCardList() {
        var board = new Board();
        board.setId(1L);
        boardService.saveBoard(board);
        var boardId = board.getId();
        var list = new CardList();
        list.setId(1L);
        boardService.saveCardList(list, boardId);
        boardService.deleteCardListById(1L, boardId);
        var boards = boardService.findAllBoards();
        assertEquals(1, boards.size());
        assertEquals(0, boards.get(0).getLists().size());
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
    void updateTest() {
        var board = new Board();
        board.setId(1L);
        boardService.saveBoard(board);
        var boardId = board.getId();

        var list = new CardList();
        list.setId(1L);
        boardService.saveCardList(list, boardId);
        var listId = list.getId();

        var boards = boardService.findAllBoards();
        assertEquals(1, boards.size());
        assertEquals(1, boards.get(0).getLists().size());

        boardService.update(new Board("test"), boardId);
        boards = boardService.findAllBoards();
        assertEquals("test", boards.get(0).getTitle());
    }

    @Test
    void testEverythingForNull() {
        assertThrows(IllegalArgumentException.class, () -> boardService.saveBoard(null));
        assertThrows(IllegalArgumentException.class, () -> boardService.saveCardList(null, 1L));
        assertThrows(IllegalArgumentException.class, () -> boardService.update(null, 1L));
    }

    @Test
    void moveCardTest() {
        var board = new Board();
        board.setId(1L);
        boardService.saveBoard(board);
        var boardId = board.getId();
        var list = new CardList();
        list.setId(1L);
        boardService.saveCardList(list, boardId);
        var listId = list.getId();

        var list2 = new CardList();
        list2.setId(2L);
        var card = new Card("test");
        list2.add(card);
        boardService.saveCardList(list2, boardId);
        var listId2 = list2.getId();
        var cardId = card.getId();

        var boards = boardService.findAllBoards();
        var lists = boards.get(0).getLists();
        assertEquals(1, boards.size());
        assertEquals(2, lists.size());
        assertEquals(0, lists.get(0).getCards().size());
        assertEquals(1, lists.get(1).getCards().size());

        boardService.moveCard(cardId, listId, 0, boardId);

        boards = boardService.findAllBoards();
        lists = boards.get(0).getLists();
        assertEquals(1, boards.size());
        assertEquals(2, lists.size());
        assertEquals(1, lists.get(0).getCards().size());
        assertEquals(0, lists.get(1).getCards().size());
    }

    @Test
    void tagAddTest() {
        Tag tag = new Tag("test");

        var board = new Board();
        board.setId(1L);
        Set<Tag> set = new HashSet<>();
        set.add(tag);
        board.setTags(set);

        boardService.saveBoard(board);
        var boardId = board.getId();

        var res = boardService.getTags(boardId);
        assertEquals(1, res.size());

        var tag2 = new Tag("test2");
        boardService.addTag(tag2, boardId);
        res = boardService.getTags(boardId);
        assertEquals(2, res.size());

        assertTrue(res.contains(tag));
        assertTrue(res.contains(tag2));
    }

    @Test
    void tagDeleteTest() {
        Tag tag = new Tag("test");

        var board = new Board();
        board.setId(1L);
        Set<Tag> set = new HashSet<>();
        set.add(tag);
        board.setTags(set);

        boardService.saveBoard(board);
        var boardId = board.getId();

        var res = boardService.getTags(boardId);
        assertEquals(1, res.size());

        boardService.deleteTag(tag.getId(), boardId);
        res = boardService.getTags(boardId);
        assertEquals(0, res.size());
    }
}
