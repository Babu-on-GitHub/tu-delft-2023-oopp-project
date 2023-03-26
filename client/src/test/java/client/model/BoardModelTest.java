package client.model;

import client.scenes.MainPageCtrl;
import client.utils.ServerUtils;
import commons.Board;
import commons.Card;
import commons.CardList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BoardModelTest {
    private Board board;
    private ServerUtils utils;
    private MainPageCtrl controller;
    private List<ListModel> children;
    private BoardModel boardModel;

    @BeforeEach
    public void setUp() {
        board = new Board("testBoard");
        utils = Mockito.mock(ServerUtils.class);
        controller = Mockito.mock(MainPageCtrl.class);
        children = new ArrayList<>();
        boardModel = new BoardModel(board, utils);
        boardModel.setController(controller);
        boardModel.setChildren(children);
    }
    @Test
    public void testUpdateChild() {

        CardList cardList1 = new CardList("Test List 1");
        when(utils.addCardList(cardList1, board)).thenReturn(java.util.Optional.of(cardList1));
        boardModel.addList(new ListModel(cardList1, boardModel, utils));

        CardList cardList2 = new CardList("Test List 2");
        when(utils.addCardList(cardList2, board)).thenReturn(java.util.Optional.of(cardList2));
        boardModel.updateChild(cardList2);

        assertEquals(cardList2, board.getLists().get(0));
    }
    @Test
    public void testAddList() {
        CardList newList = new CardList("testList");
        when(utils.addCardList(newList, board)).thenReturn(java.util.Optional.of(newList));
        ListModel listModel = new ListModel(newList, boardModel, utils);
        boardModel.addList(listModel);

        assertTrue(board.getLists().contains(newList));
        assertTrue(children.contains(listModel));
    }

    @Test
    public void testDeleteList() {
        CardList newList = new CardList("testList");
        when(utils.addCardList(newList, board)).thenReturn(java.util.Optional.of(newList));
        ListModel listModel = new ListModel(newList, boardModel, utils);
        boardModel.addList(listModel);
        boardModel.deleteList(listModel);

        assertFalse(board.getLists().contains(newList));
        assertFalse(children.contains(listModel));

    }

    @Test
    public void testUpdate() {
        Board serverBoard = new Board("testBoard");
        when(utils.getBoardById(board.getId())).thenReturn(java.util.Optional.of(serverBoard));
        when(utils.updateBoardById(board.getId(), board)).thenReturn(java.util.Optional.of(board));
        boolean result = boardModel.update();
        assertFalse(result);
        verify(utils).getBoardById(board.getId());
    }

    @Test
    public void getBoardTest() {
        assertEquals(board,boardModel.getBoard());
    }

    @Test
    public void setBoardTest() {
        board = new Board("testBoard123");
        boardModel.setBoard(board);
        assertEquals(boardModel.getBoard(),board);
    }

    @Test
    void testMoveCard() {
        CardList cardList1 = new CardList("List 1");
        when(utils.addCardList(cardList1, board)).thenReturn(java.util.Optional.of(cardList1));
        boardModel.addList(new ListModel(cardList1, boardModel, null));
        CardList cardList2 = new CardList("List 2");
        when(utils.addCardList(cardList2, board)).thenReturn(java.util.Optional.of(cardList2));
        boardModel.addList(new ListModel(cardList2, boardModel, null));
        children = boardModel.getChildren();

        Card card = new Card("Card 1");
        CardModel cardModel1 = new CardModel(card, children.get(1), null);
        when(utils.addCard(card, children.get(0).getCardList())).thenReturn(java.util.Optional.of(cardModel1.getCard()));
        utils.addCard(cardModel1.getCard(), children.get(0).getCardList());
        cardList1.add(cardModel1.getCard());

        boardModel.moveCard(cardModel1, children.get(1),0);

        assertEquals(0, children.get(0).getChildren().size());
        assertEquals(1, children.get(1).getChildren().size());
        assertEquals(cardModel1, children.get(1).getChildren().get(0));
    }
}
