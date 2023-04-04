package server.api.api;

import commons.Card;
import commons.CardList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.api.CardListController;
import server.services.CardListService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CardListControllerTest {

    private CardListService cardListService;
    private CardListController cardListController;

    @BeforeEach
    public void setUp() {
        cardListService = mock(CardListService.class);
        cardListController = new CardListController(cardListService);
    }

    @Test
    public void testGetAll() {
        CardList list1 = new CardList(1, "List 1", Arrays.asList(new Card()));
        CardList list2 = new CardList(2, "List 2", Arrays.asList(new Card()));
        List<CardList> lists = Arrays.asList(list1, list2);
        when(cardListService.getAllCardLists()).thenReturn(lists);

        List<CardList> response = cardListController.getAll();
        assertEquals(lists, response);
    }
    @Test
    public void testGetById() {
        long listId = 1;
        CardList list = new CardList(listId, "List 1", Arrays.asList(new Card()));
        when(cardListService.getCardListById(listId)).thenReturn(list);

        ResponseEntity<CardList> response = cardListController.getById(listId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(list, response.getBody());
    }

    @Test
    public void testGetByIdWithInvalidId() {
        long invalidId = 100;
        String errorMessage = "Card list not found with id " + invalidId;
        when(cardListService.getCardListById(invalidId)).thenThrow(new IllegalArgumentException(errorMessage));

        ResponseEntity<CardList> response = cardListController.getById(invalidId);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testAdd() {
        long listId = 1;
        Card card = new Card(1, "Test Card", "Description", null, null);
        Card addedCard = new Card(1, "Test Card", "Description", null, null);
        when(cardListService.addCard(card, listId)).thenReturn(addedCard);

        ResponseEntity<Card> response = cardListController.add(card, listId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(addedCard, response.getBody());
    }
    @Test
    public void testAddWithInvalidListId() {
        long invalidId = 100;
        Card card = new Card(1, "Test Card", "Description", null, null);
        String errorMessage = "Card list not found with id " + invalidId;
        when(cardListService.addCard(any(Card.class), any(Long.class))).thenThrow(new IllegalArgumentException(errorMessage));

        ResponseEntity<Card> response = cardListController.add(card, invalidId);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testRemove() {
        long cardId = 1L;
        long listId = 2L;
        doNothing().when(cardListService).removeCard(cardId, listId);

        ResponseEntity<Boolean> response = cardListController.remove(cardId, listId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody());
    }
    @Test
    public void testRemoveListWithException() {
        long invalidId = -10;
        long invalidBoardId = -10;
        when(cardListController.remove(invalidId,invalidBoardId)).thenThrow(new IllegalArgumentException());

        ResponseEntity<Boolean> response = cardListController.remove(invalidId,invalidBoardId);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testUpdate() {
        long id = 1L;
        CardList cardList = new CardList(id, "Test List", Arrays.asList(new Card(1L, "Test Card", "Description", null, null)));
        when(cardListService.update(cardList, id)).thenReturn(cardList);

        ResponseEntity<CardList> response = cardListController.update(cardList, id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cardList, response.getBody());
    }

    @Test
    public void testUpdateWithInvalidId() {
        long id = 100L;
        CardList cardList = new CardList(id, "Test List", Arrays.asList(new Card(1L, "Test Card", "Description", null, null)));
        when(cardListService.update(cardList, id)).thenThrow(new IllegalArgumentException());

        ResponseEntity<CardList> response = cardListController.update(cardList, id);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
    @Test
    public void updateListTitleWithException() {
        long invalidId = -10;
        long invalidBoardId = -10;
        when(cardListController.updateTitle("New",-1)).thenThrow(new IllegalArgumentException());

        ResponseEntity<String> response = cardListController.updateTitle("New",-1);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
