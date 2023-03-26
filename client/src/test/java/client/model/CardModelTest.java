package client.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import client.scenes.CardController;
import client.utils.ServerUtils;
import commons.Card;
import commons.CardList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class CardModelTest {

    private CardModel cardModel;
    private Card card;
    private ListModel parent;
    private ServerUtils serverUtils;
    private CardController controller;

    @BeforeEach
    public void setUp() {
        card = mock(Card.class);
        parent = mock(ListModel.class);
        serverUtils = mock(ServerUtils.class);
        cardModel = new CardModel(card, parent, serverUtils);
        controller = mock(CardController.class);
    }

    @Test
    public void testConstructor() {
        assertNotNull(cardModel);
        assertEquals(card, cardModel.getCard());
        assertEquals(parent, cardModel.getParent());
        assertEquals(serverUtils, cardModel.getUtils());
    }

    @Test
    public void testSetCard() {
        Card newCard = mock(Card.class);
        cardModel.setCard(newCard);
        assertEquals(newCard, cardModel.getCard());
    }

    @Test
    public void testDeleteCard() {
        CardModel spyCardModel = spy(cardModel);
        doNothing().when(parent).deleteCard(spyCardModel);
        spyCardModel.deleteCard();
        verify(parent, times(1)).deleteCard(spyCardModel);
    }

    @Test
    public void testGetController() {
        CardController cardController = new CardController();
        cardModel.setController(cardController);
        assertEquals(cardController, cardModel.getController());
    }

    @Test
    public void testSetController() {
        CardController cardController = new CardController();
        cardModel.setController(cardController);
        assertEquals(cardController, cardModel.getController());
    }

    @Test
    public void testUpdate() {
        CardModel spyCardModel = spy(cardModel);
        Card newCard = mock(Card.class);
        when(serverUtils.updateCardById(card.getId(), card)).thenReturn(java.util.Optional.of(newCard));
        when(parent.getCardList()).thenReturn(mock(CardList.class));
        when(serverUtils.addCard(card, parent.getCardList())).thenReturn(java.util.Optional.of(newCard));
        when(serverUtils.getCardById(card.getId())).thenReturn(java.util.Optional.empty());
        spyCardModel.update();
        verify(serverUtils, times(1)).addCard(card, parent.getCardList());
    }
    @Test
    public void testDisown() {
        CardList cardList = mock(CardList.class);
        when(parent.getCardList()).thenReturn(cardList);
        when(cardList.getCards()).thenReturn(new ArrayList<>(List.of(card)));

        cardModel.disown();

        assertEquals(null, cardModel.getParent());
    }

    @Test
    public void testFosterBy() {
        CardList cardList = mock(CardList.class);
        ListModel newParent = mock(ListModel.class);
        List<CardModel> children = new ArrayList<>(List.of(cardModel));
        when(parent.getCardList()).thenReturn(cardList);
        when(cardList.getCards()).thenReturn(new ArrayList<>(List.of(card)));
        when(newParent.getChildren()).thenReturn(children);
        when(newParent.getCardList()).thenReturn(cardList);

        cardModel.fosterBy(newParent, 0);

        assertEquals(newParent, cardModel.getParent());
    }
}
