package client.model;

import client.scenes.ListController;
import client.scenes.MainPageCtrl;
import client.utils.ServerUtils;
import commons.Board;
import commons.Card;
import commons.CardList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ListModelTest {
    static Logger log = Logger.getLogger(ListModel.class.getName());
    private CardModel cardModel;
    private Board board;
    private Card card;
    private ServerUtils utils;
    private MainPageCtrl controller;
    private List<ListModel> children;
    private BoardModel boardModel;
    private ListModel listModel;
    private CardList cardList;
    private CardModel card1;
    private CardModel card2;
    private CardModel card3;

    @BeforeEach
    public void setUp() {
        board = new Board("testBoard");
        card = mock(Card.class);
        utils = mock(ServerUtils.class);
        controller = mock(MainPageCtrl.class);
        children = new ArrayList<>();
        boardModel =  mock(BoardModel.class);
        cardModel = new CardModel(card,listModel , utils);
        cardList = new CardList();
        listModel = new ListModel(cardList, boardModel, utils);


    }
    @Test
    public void testGetSetController() {
        ListController listController = new ListController();
        listModel.setController(listController);
        assertEquals(listController, listModel.getController());
    }
    @Test
    public void testGetSetBoardTest() {
        assertEquals(listModel.getParent(),boardModel);
    }
    @Test
    public void testAddCard() {
        CardList cardList1 = new CardList("List 1");
        when(utils.addCardList(cardList1, board)).thenReturn(Optional.of(cardList1));
        boardModel.addList(new ListModel(cardList1, boardModel, null));
        Card card = new Card("Card 1");
        CardModel cardModel1 = new CardModel(card, listModel, null);
        listModel.addCard(cardModel1);

        assertTrue(cardModel1.getParent().equals(listModel));
    }


    @Test
    public void testInsertCard() {
        cardList = new CardList( "List 1");
        card1 = new CardModel(new Card("Card 1"),listModel,utils);
        card2 = new CardModel(new Card("Card 2"),listModel,utils);
        card3 = new CardModel(new Card("Card 3"),listModel,utils);

        cardList.getCards().add(card1.getCard());
        cardList.getCards().add(card2.getCard());
        cardList.getCards().add(card3.getCard());
        listModel.insertCard(cardModel, 0);
        assertEquals(cardList.getCards().get(0), card1.getCard());
        assertEquals(cardList.getCards().size(), 3);
    }
    @Test
    public void testInsertCardAtPosition() {
        cardList = new CardList( "List 1");
        card1 = new CardModel(new Card("Card 1"),listModel,utils);
        card2 = new CardModel(new Card("Card 2"),listModel,utils);
        card3 = new CardModel(new Card("Card 3"),listModel,utils);

        cardList.getCards().add(card1.getCard());
        cardList.getCards().add(card2.getCard());
        cardList.getCards().add(card3.getCard());
        listModel.insertCard(cardModel,0);
        listModel.insertCard(cardModel, 1);
        assertEquals(cardList.getCards().get(1), card2.getCard());
        assertEquals(cardList.getCards().size(), 3);
    }

    @Test
    public void moveCardTest() {
        var card1test = new Card("testCard1");
        var card2test = new Card("testCard2");
        var card3test = new Card("testCard3");
        var cardModel1 = new CardModel(card1test, listModel,utils);
        var cardModel2 = new CardModel(card2test, listModel,utils);
        var cardModel3 = new CardModel(card3test, listModel,utils);
        listModel.insertCard(cardModel1, 0);
        listModel.insertCard(cardModel2, 1);
        listModel.insertCard(cardModel3, 2);

        listModel.moveCard(0, 2);
        assertEquals(cardList.getCards().get(0), card2test);
        assertEquals(cardList.getCards().get(1), card3test);
        assertEquals(cardList.getCards().get(2), card1test);

        listModel.moveCard(2, 1);
        assertEquals(cardList.getCards().get(0), card2test);
        assertEquals(cardList.getCards().get(1), card1test);
        assertEquals(cardList.getCards().get(2), card3test);
    }

    @Test
    public void testDeleteCard() {
        var card1test = new Card("testCard1");
        var cardToDelete = new CardModel(card1test, listModel,utils);
        listModel.insertCard(cardToDelete, 0);
        listModel.deleteCard(cardToDelete);
        assertFalse(cardList.getCards().contains(cardToDelete.getCard()));
    }

    @Test
    public void testDeleteCardIdNotFound() {
        cardList = new CardList( "List 1");
        card1 = new CardModel(new Card("Card 1"),listModel,utils);
        card2 = new CardModel(new Card("Card 2"),listModel,utils);
        card3 = new CardModel(new Card("Card 3"),listModel,utils);
        cardList.getCards().add(card1.getCard());
        cardList.getCards().add(card2.getCard());
        cardList.getCards().add(card3.getCard());
        CardModel cardToDelete = new CardModel(new Card( "Card 4"),listModel,utils);

        listModel.deleteCard(cardToDelete);

        assertTrue(cardList.getCards().contains(card1.getCard()));
        assertTrue(cardList.getCards().contains(card2.getCard()));
        assertTrue(cardList.getCards().contains(card3.getCard()));
    }

    @Test
    public void testDeleteCardById() {
        cardList = new CardList( "List 1");
        card1 = new CardModel(new Card(1,"Card 1","Description",null,null),listModel,utils);
        card2 = new CardModel(new Card(2,"Card 2","Description",null,null),listModel,utils);
        card3 = new CardModel(new Card(3,"Card 3","Description",null,null),listModel,utils);
        listModel.getCardList().getCards().add(card1.getCard());
        listModel.getCardList().getCards().add(card2.getCard());
        listModel.getCardList().getCards().add(card3.getCard());
        long idToDelete = card2.getCard().getId();

        listModel.deleteCardById(idToDelete);

        assertFalse(listModel.getCardList().getCards().stream().anyMatch(c -> c.getId() == idToDelete));
    }

    @Test
    public void testDeleteByIDIDnotfound() {
        cardList = new CardList( "List 1");
        card1 = new CardModel(new Card(1,"Card 1","Description",null,null),listModel,utils);
        card2 = new CardModel(new Card(2,"Card 2","Description",null,null),listModel,utils);
        card3 = new CardModel(new Card(3,"Card 3","Description",null,null),listModel,utils);
        listModel.getCardList().getCards().add(card1.getCard());
        listModel.getCardList().getCards().add(card2.getCard());
        listModel.getCardList().getCards().add(card3.getCard());
        long idToDelete = 5;

        listModel.deleteCardById(idToDelete);

        assertTrue(listModel.getCardList().getCards().contains(card1.getCard()));
        assertTrue(listModel.getCardList().getCards().contains(card2.getCard()));
        assertTrue(listModel.getCardList().getCards().contains(card3.getCard()));
    }

    @Test
    public void testUpdate() {
        when(utils.updateCardListById(cardList.getId(), cardList)).thenReturn(java.util.Optional.of(cardList));
        boolean result = listModel.update(true);
        assertFalse(result);
        verify(utils).getCardListById(cardList.getId());
    }
}


