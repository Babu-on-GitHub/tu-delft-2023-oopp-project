package server.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import commons.Card;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.services.CardService;

public class CardControllerTest {

    private CardService cardService;
    private CardController cardController;
    @BeforeEach
    public void setUp() {
        cardService = mock(CardService.class);
        cardController = new CardController(cardService);
    }

    @Test
    public void testGetAll() {
        Card card1 = new Card(1, "Card 1", "Description 1", null, null);
        Card card2 = new Card(2, "Card 2", "Description 2", null, null);
        List<Card> cards = Arrays.asList(card1, card2);
        when(cardService.getAllCards()).thenReturn(cards);

        List<Card> response = cardController.getAll();
        assertEquals(cards, response);
    }

    @Test
    public void testGetById() {
        Card card = new Card(1, "Test Card","Description",null,null);
        when(cardService.getCardById(card.getId())).thenReturn(card);

        ResponseEntity<Card> response = cardController.getById(card.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(card, response.getBody());
    }

    @Test
    public void testGetByIdWithInvalidId() {
        long invalidId = 100;
        when(cardService.getCardById(invalidId)).thenThrow(new IllegalArgumentException());

        ResponseEntity<Card> response = cardController.getById(invalidId);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testUpdate() {
        Card card = new Card(1, "Test Card","Description",null,null);
        when(cardService.update(card, card.getId())).thenReturn(card);

        ResponseEntity<Card> response = cardController.update(card, card.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(card, response.getBody());
    }
    @Test
    public void testUpdateWithInvalidId() {
        Card card = new Card(1, "Test Card","Description",null,null);
        long invalidId = 100;
        when(cardService.update(card, invalidId)).thenThrow(new IllegalArgumentException());

        ResponseEntity<Card> response = cardController.update(card, invalidId);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


}
