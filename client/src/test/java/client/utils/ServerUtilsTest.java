package client.utils;

import commons.Board;
import commons.Card;
import commons.CardList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ServerUtilsTest {
    private TestServerUtils serverUtils;

    @BeforeEach
    public void setUp() {
        serverUtils = new TestServerUtils();
    }

    @Test
    void serverUtilsTest() {
        assertNotNull(serverUtils);
    }

    @Test
    void getServerUtils() {
        assertNotNull(serverUtils.getSocketUtils());
    }

    @Test
    void chooseServer() {
        String server = "I like apples";
        assertFalse(serverUtils.chooseServer(server));
    }

    @Test
    void setServerUtils() {
        SocketUtils socketUtils = new TestSocketUtils();
        serverUtils.setSocketUtils(socketUtils);
        assertEquals(socketUtils, serverUtils.getSocketUtils());
    }

    @Test
    void wrapWithHttp() {
        String server = "I like apples";
        assertEquals("http://" + server, serverUtils.wrapWithHttp(server));
    }

    @Test
    void getCards() {
        serverUtils.getCards();
        assertTrue(serverUtils.wasMade("get", "api/card"));
    }

    @Test
    void getCardById() {
        serverUtils.getCardById(1);
        assertTrue(serverUtils.wasMade("get", "api/card/1"));
    }

    @Test
    void addCard() {
        CardList list = new CardList();
        list.setId(5);

        Card card = new Card();
        serverUtils.addCard(card, list);
        assertTrue(serverUtils.wasMade("post", "api/list/add/" + list.getId()));
    }

    @Test
    void deleteCardById() {
        long listId = 1, cardId = 2;
        serverUtils.deleteCardById(cardId, listId);
        assertTrue(serverUtils.wasMade("delete", "api/list/delete/" + cardId + "/from/" + listId));
    }

    @Test
    void insertCard() {
        CardList list = new CardList();
        list.setId(5);

        Card card = new Card();
        serverUtils.insertCard(card, 1, list);
        assertTrue(serverUtils.wasMade("post", "api/list/insert/" + list.getId() + "/to/1"));
    }

    @Test
    void updateCardById() {
        long cardId = 2;
        Card card = new Card();
        serverUtils.updateCardById(cardId, card);
        assertTrue(serverUtils.wasMade("put", "api/card/update/" + cardId));
    }

    @Test
    void getCardLists() {
        serverUtils.getCardLists();
        assertTrue(serverUtils.wasMade("get", "api/list"));
    }

    @Test
    void getCardListById() {
        long listId = 1;
        serverUtils.getCardListById(listId);
        assertTrue(serverUtils.wasMade("get", "api/list/" + listId));
    }

    @Test
    void addCardList() {
        CardList list = new CardList();
        Board board = new Board();
        board.setId(5);
        serverUtils.addCardList(list, board);
        assertTrue(serverUtils.wasMade("post", "api/board/add/" + board.getId()));
    }

    @Test
    void deleteCardListById() {
        long listId = 1;
        long boardId = 5;
        serverUtils.deleteCardListById(listId, boardId);
        assertTrue(serverUtils.wasMade("delete", "api/board/delete/" + listId + "/from/" + boardId));
    }

    @Test
    void updateCardListById() {
        long listId = 1;
        CardList list = new CardList();
        serverUtils.updateCardListById(listId, list);
        assertTrue(serverUtils.wasMade("put", "api/list/update/" + listId));
    }

    @Test
    void getBoards() {
        serverUtils.getBoards();
        assertTrue(serverUtils.wasMade("get", "api/board"));
    }

    @Test
    void getBoardById() {
        long boardId = 1;
        serverUtils.getBoardById(boardId);
        assertTrue(serverUtils.wasMade("get", "api/board/" + boardId));
    }

    @Test
    void addBoard() {
        Board board = new Board();
        serverUtils.addBoard(board);
        assertTrue(serverUtils.wasMade("post", "api/board/create"));
    }

    @Test
    void deleteBoardById() {
        long boardId = 1;
        serverUtils.deleteBoardById(boardId);
        assertTrue(serverUtils.wasMade("delete", "api/board/delete/" + boardId));
    }

    @Test
    void updateBoardById() {
        long boardId = 1;
        Board board = new Board();
        serverUtils.updateBoardById(boardId, board);
        assertTrue(serverUtils.wasMade("put", "api/board/update/" + boardId));
    }

    @Test
    void moveCard() {
        long cardId = 1, listId = 2, position = 3, boardId = 4;
        serverUtils.moveCard(cardId, listId, (int)position, boardId);
        assertTrue(serverUtils.wasMade("post", "api/board/moveCard/" + cardId + "/to/" + listId +
                "/at/" + position + "/located/" + boardId));
    }

}
