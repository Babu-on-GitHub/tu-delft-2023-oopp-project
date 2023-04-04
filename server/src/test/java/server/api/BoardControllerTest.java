package server.api;

import commons.Board;
import commons.Card;
import commons.CardList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.services.BoardService;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BoardControllerTest {

    @Mock
    private BoardService boardService;

    private BoardController boardController;
    @Mock
    private Logger logger;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        boardController = new BoardController(boardService);
    }

    @Test
    public void testGetAll() {
        List<Board> expectedBoards = new ArrayList<>();
        when(boardService.findAllBoards()).thenReturn(expectedBoards);

        List<Board> actualBoards = boardController.getAll();

        verify(boardService, times(1)).findAllBoards();
        assertEquals(expectedBoards, actualBoards);
    }

    @Test
    public void testGetById_success() {
        long boardId = 1234L;
        Board expectedBoard = new Board();
        expectedBoard.setId(boardId);
        when(boardService.getBoardById(boardId)).thenReturn(expectedBoard);

        ResponseEntity<Board> response = boardController.getById(boardId);

        verify(boardService, times(1)).getBoardById(boardId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBoard, response.getBody());
    }

    @Test
    public void testGetById_failure() {
        long boardId = 1234L;
        String errorMessage = "Board not found";
        when(boardService.getBoardById(boardId)).thenThrow(new IllegalArgumentException(errorMessage));

        ResponseEntity<Board> response = boardController.getById(boardId);

        verify(boardService, times(1)).getBoardById(boardId);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testCreate_success() {
        Board boardToCreate = new Board();
        boardToCreate.setTitle("Test Board");
        Board expectedBoard = new Board();
        expectedBoard.setId(5678L);
        expectedBoard.setTitle("Test Board");
        when(boardService.saveBoard(boardToCreate)).thenReturn(expectedBoard);

        ResponseEntity<Board> response = boardController.create(boardToCreate);

        verify(boardService, times(1)).saveBoard(boardToCreate);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBoard, response.getBody());
    }

    @Test
    public void testCreate_failure() {
        Board boardToCreate = new Board();
        boardToCreate.setTitle("Test Board");
        String errorMessage = "Failed to create board";
        when(boardService.saveBoard(boardToCreate)).thenThrow(new IllegalArgumentException(errorMessage));

        ResponseEntity<Board> response = boardController.create(boardToCreate);

        verify(boardService, times(1)).saveBoard(boardToCreate);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testAddList() {
        CardList list = new CardList();
        long boardId = 1;
        when(boardService.saveCardList(list, boardId)).thenReturn(list);
        ResponseEntity<CardList> response = boardController.add(list, boardId);
        verify(boardService, times(1)).saveCardList(list, boardId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(list, response.getBody());
    }
    @Test
    public void testAddWithInvalidListId() {
        List<Card> cards = new ArrayList<>();
        cards.add(new Card("New Card"));
        long invalidId = 100;
        CardList cardList = new CardList(1, "New",cards);
        when(boardController.add(cardList, invalidId)).thenThrow(new IllegalArgumentException());

        ResponseEntity<CardList> response = boardController.add(cardList, invalidId);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testDeleteBoard() {
        long boardId = 1;
        doNothing().when(boardService).deleteBoardById(boardId);
        ResponseEntity<Boolean> response = boardController.delete(boardId);
        verify(boardService, times(1)).deleteBoardById(boardId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
    }

    @Test
    public void testDeleteBoardWithException() {
        long invalidId = 100;
        when(boardController.delete(invalidId)).thenThrow(new IllegalArgumentException());

        ResponseEntity<Boolean> response = boardController.delete(invalidId);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testRemoveList() {
        long listId = 1;
        long boardId = 2;
        doNothing().when(boardService).deleteCardListById(listId, boardId);
        ResponseEntity<Boolean> response = boardController.remove(listId, boardId);
        verify(boardService, times(1)).deleteCardListById(listId, boardId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
    }

    @Test
    public void testRemoveListWithException() {
        long invalidId = -10;
        long invalidBoardId = -10;
        when(boardController.remove(invalidId,invalidBoardId)).thenThrow(new IllegalArgumentException());

        ResponseEntity<Boolean> response = boardController.remove(invalidId,invalidBoardId);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
    @Test
    void testUpdateSuccess() {
        long boardId = 1234;
        Board board = new Board();
        board.setId(boardId);

        when(boardService.update(board, boardId)).thenReturn(board);

        ResponseEntity<Board> response = boardController.update(board, boardId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(board, response.getBody());
        verify(boardService).update(board, boardId);
    }
    @Test
    public void moveCardWithException() {
        long invalidId = -10;
        long invalidBoardId = -10;
        when(boardController.moveCard(invalidId,-1,-1,invalidBoardId)).thenThrow(new IllegalArgumentException());

        ResponseEntity<Boolean> response = boardController.moveCard(invalidId,-1,-1,invalidBoardId);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void updateBoardWithException() {
        Board board = new Board(1, "New board", null);
        when(boardController.update(board,-1)).thenThrow(new IllegalArgumentException());

        ResponseEntity<Board> response = boardController.update(board,-1);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void updateBoardTitleWithException() {
        long invalidId = -10;
        long invalidBoardId = -10;
        when(boardController.updateTitle("New",-1)).thenThrow(new IllegalArgumentException());

        ResponseEntity<String> response = boardController.updateTitle("New",-1);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}