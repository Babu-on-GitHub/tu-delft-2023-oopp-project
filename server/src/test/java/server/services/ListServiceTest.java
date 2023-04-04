package server.services;

import commons.Card;
import commons.CardList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.repository.TestCardListRepository;

import static org.junit.jupiter.api.Assertions.*;

public class ListServiceTest {

    private CardListService listService;

    @BeforeEach
    public void setup() {
        listService = new CardListService(new TestCardListRepository(), new TestSynchronizationService());
    }

    @Test
    void getCardListById() {
        // should throw illegal argument
        assertThrows(IllegalArgumentException.class, () -> listService.getCardListById(1L));
    }

    @Test
    void getExistingCardListById() {
        var list = new CardList();
        list.setId(1L);
        listService.saveCardList(list);
        var listId = list.getId();
        var foundList = listService.getCardListById(listId);
        assertEquals(listId, foundList.getId());
    }

    @Test
    void saveCardList() {
        var list = new CardList();
        list.setId(1L);
        listService.saveCardList(list);
        var lists = listService.getAllCardLists();
        assertEquals(1, lists.size());
    }

    @Test
    void getAllCardLists() {
        var lists = listService.getAllCardLists();
        assertEquals(0, lists.size());
    }

    @Test
    void removeCard() {
        var list = new CardList();
        list.setId(1L);
        listService.saveCardList(list);
        var listId = list.getId();

        var card = new Card();
        listService.addCard(card, listId);
        var cardId = card.getId();

        listService.removeCard(cardId, listId);
        var lists = listService.getAllCardLists();
        assertEquals(0, lists.get(0).getCards().size());
    }

    @Test
    void addCard() {
        var list = new CardList();
        list.setId(1L);
        listService.saveCardList(list);
        var listId = list.getId();

        var card = new Card();
        listService.addCard(card, listId);
        var cardId = card.getId();

        var lists = listService.getAllCardLists();
        assertEquals(1, lists.size());
        assertEquals(1, lists.get(0).getCards().size());
        assertEquals(cardId, lists.get(0).getCards().get(0).getId());
    }

    @Test
    void updateCardListById() {
        var list = new CardList();
        list.setId(1L);
        listService.saveCardList(list);
        var listId = list.getId();

        var card = new Card();
        listService.addCard(card, listId);
        var cardId = card.getId();

        var updatedList = new CardList();
        updatedList.setId(listId);
        updatedList.setTitle("updated");
        updatedList.setCards(list.getCards());
        listService.update(updatedList, listId);

        var lists = listService.getAllCardLists();
        assertEquals(1, lists.size());
        assertEquals("updated", lists.get(0).getTitle());
    }

    @Test
    void updateCardListByIdWithNewCard() {
        var list = new CardList();
        list.setId(1L);
        listService.saveCardList(list);
        var listId = list.getId();

        var card = new Card();
        listService.addCard(card, listId);
        var cardId = card.getId();

        var updatedList = new CardList();
        updatedList.setId(listId);
        updatedList.setTitle("updated");
        updatedList.setCards(list.getCards());

        var newCard = new Card();
        updatedList.getCards().add(newCard);

        listService.update(updatedList, listId);

        var lists = listService.getAllCardLists();
        assertEquals(1, lists.size());
        assertEquals("updated", lists.get(0).getTitle());
        assertEquals(2, lists.get(0).getCards().size());
    }

    @Test
    void updateCardListByIdWithRemovedCard() {
        var list = new CardList();
        list.setId(1L);
        listService.saveCardList(list);
        var listId = list.getId();

        var card = new Card();
        listService.addCard(card, listId);
        var cardId = card.getId();

        var updatedList = new CardList();
        updatedList.setId(listId);
        updatedList.setTitle("updated");
        updatedList.setCards(list.getCards());

        updatedList.getCards().remove(0);

        listService.update(updatedList, listId);

        var lists = listService.getAllCardLists();
        assertEquals(1, lists.size());
        assertEquals("updated", lists.get(0).getTitle());
        assertEquals(0, lists.get(0).getCards().size());
    }


    @Test
    public void updateBoardTitleWithException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            listService.updateTitle(1, "new");
        });

        String expectedMessage = "Trying to get non-existing card list";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage,actualMessage);
    }
    @Test
    void testEverythingWithNull() {
        assertThrows(IllegalArgumentException.class, () -> listService.addCard(null, 0));
        assertThrows(IllegalArgumentException.class, () -> listService.update(null, 0));
    }

    @Test
    void updateTitleTest() {
        var list = new CardList();
        list.setId(1L);
        listService.saveCardList(list);
        var listId = list.getId();

        listService.updateTitle(listId, "new title");
        var lists = listService.getAllCardLists();
        assertEquals("new title", lists.get(0).getTitle());
    }
}
