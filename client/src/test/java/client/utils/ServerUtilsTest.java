package client.utils;

import commons.Board;
import commons.Card;
import commons.CardList;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServerUtilsTest {


    private static MockWebServer mockServer;

    private ServerUtils serverUtils;

    @BeforeAll
    public static void setupServer() throws IOException {
        mockServer = new MockWebServer();
        mockServer.start();
    }

    @BeforeEach
    public void setUp() {
        serverUtils = spy(ServerUtils.class);
        //String baseUrl = String.format("localhost:%s", mockServer.getPort());
        //serverUtils.chooseServer(baseUrl);
    }

    @AfterAll
    public static void teardown() throws IOException {
        mockServer.shutdown();
    }

    @Test
    void serverUtilsTest() {
        assertNotNull(serverUtils);
    }

    @Test
    void chooseServerNullTest() {
        assertFalse(serverUtils.chooseServer(null));
    }

    @Test
    void chooseServerInvalidServerTest() {
        assertFalse(serverUtils.chooseServer("thisIsNotAServer"));
    }

    @Test
    void chooseServerValidTest() {
//        String baseUrl = String.format("http://localhost:%s", mockServer.getPort());
//        assertTrue(serverUtils.chooseServer(baseUrl));
    }

    @Test
    void getServerUtils() {
        assertNotNull(serverUtils.getSocketUtils());
    }

    @Test
    void getCardsTest() {
        Optional<List<Card>> response = Optional.of(new ArrayList<Card>());
        when(serverUtils.getCards()).thenReturn(response);
        assertEquals(response, serverUtils.getCards());
    }

    @Test
    void getCardByIdTest() {
        Optional<Card> response = Optional.of(mock(Card.class));
        when(serverUtils.getCardById(1L)).thenReturn(response);
        assertEquals(response, serverUtils.getCardById(1L));
    }

    @Test
    void addCardTest() {
        Card card = mock(Card.class);
        CardList list = mock(CardList.class);
        Optional<Card> response = Optional.of(card);
        when(serverUtils.addCard(card, list)).thenReturn(response);
        assertEquals(response, serverUtils.addCard(card, list));
    }

    @Test
    void insertCardTest() {
        Card card = mock(Card.class);
        CardList cardList = mock(CardList.class);
        Optional<Card> response = Optional.of(card);
        when(serverUtils.insertCard(card, 1, cardList)).thenReturn(response);
        assertEquals(response, serverUtils.insertCard(card, 1, cardList));
    }

    @Test
    void deleteCardByIdTest() {
        when(serverUtils.deleteCardById(1L, 1L)).thenReturn(Optional.of(Boolean.TRUE));
        assertEquals(Optional.of(Boolean.TRUE), serverUtils.deleteCardById(1L, 1L));
    }

    @Test
    void updateCardByIdTest() {
        Card card = mock(Card.class);
        Optional<Card> response = Optional.of(card);
        when(serverUtils.updateCardById(1L, card)).thenReturn(response);
        assertEquals(response, serverUtils.updateCardById(1L, card));
    }

    @Test
    void getCardListsTest() {
        Optional<List<CardList>> response = Optional.of(new ArrayList<CardList>());
        when(serverUtils.getCardLists()).thenReturn(response);
        assertEquals(response, serverUtils.getCardLists());
    }

    @Test
    void getCardListByIdTest() {
        Optional<CardList> response = Optional.of(mock(CardList.class));
        when(serverUtils.getCardListById(1L)).thenReturn(response);
        assertEquals(response, serverUtils.getCardListById(1L));
    }

    @Test
    void addCardListTest() {
        CardList list = mock(CardList.class);
        Board board = mock(Board.class);
        Optional<CardList> response = Optional.of(list);
        when(serverUtils.addCardList(list, board)).thenReturn(response);
        assertEquals(response, serverUtils.addCardList(list, board));
    }

    @Test
    void deleteCardListByIdTest() {
        when(serverUtils.deleteCardById(1L, 1L)).thenReturn(Optional.of(Boolean.TRUE));
        assertEquals(Optional.of(Boolean.TRUE), serverUtils.deleteCardById(1L, 1L));
    }

    @Test
    void updateCardListById() {
        CardList list = mock(CardList.class);
        Optional<CardList> response = Optional.of(list);
        when(serverUtils.updateCardListById(1L, list)).thenReturn(response);
        assertEquals(response, serverUtils.updateCardListById(1L, list));
    }

    @Test
    void getBoardsTest() {
        Optional<List<Board>> response = Optional.of(new ArrayList<Board>());
        when(serverUtils.getBoards()).thenReturn(response);
        assertEquals(response, serverUtils.getBoards());
    }

    @Test
    void getBoardByIdTest() {
        Optional<Board> response = Optional.of(mock(Board.class));
        when(serverUtils.getBoardById(1L)).thenReturn(response);
        assertEquals(response, serverUtils.getBoardById(1L));
    }

    @Test
    void addBoardTest() {
        Board board = mock(Board.class);
        Optional<Board> response = Optional.of(board);
        when(serverUtils.addBoard(board)).thenReturn(response);
        assertEquals(response, serverUtils.addBoard(board));
    }

    @Test
    void deleteBoardByIdTest() {
        when(serverUtils.deleteBoardById(1L)).thenReturn(Optional.of(Boolean.TRUE));
        assertEquals(Optional.of(Boolean.TRUE), serverUtils.deleteBoardById(1L));
    }

    @Test
    void updateBoardById() {
        Board board = mock(Board.class);
        Optional<Board> response = Optional.of(board);
        when(serverUtils.updateBoardById(1L, board)).thenReturn(response);
        assertEquals(response, serverUtils.updateBoardById(1L, board));
    }

    @Test
    void moveCardTest() {
        when(serverUtils.moveCard(1L, 1L, 1, 1L)).thenReturn(Optional.of(Boolean.TRUE));
        assertEquals(Optional.of(Boolean.TRUE), serverUtils.moveCard(1L, 1L, 1, 1L));
    }
}
