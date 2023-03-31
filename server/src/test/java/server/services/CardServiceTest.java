package server.services;

import commons.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.repository.TestCardRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CardServiceTest {

    private CardService cardService;

    @BeforeEach
    public void setup() {
        cardService = new CardService(new TestCardRepository(), new TestSynchronizationService());
    }

    @Test
    void getCardById() {
        // should throw illegal argument
        assertThrows(IllegalArgumentException.class, () -> cardService.getCardById(1L));
    }

    @Test
    void getExistingCardById() {
        var card = new Card();
        card.setId(1L);
        cardService.saveCard(card);
        var cardId = card.getId();
        var foundCard = cardService.getCardById(cardId);
        assertEquals(cardId, foundCard.getId());
    }

    @Test
    void saveCard() {
        var card = new Card();
        card.setId(1L);
        cardService.saveCard(card);
        var cards = cardService.getAllCards();
        assertEquals(1, cards.size());
    }

    @Test
    void getAllCards() {
        var cards = cardService.getAllCards();
        assertEquals(0, cards.size());
    }

    @Test
    void updateCard() {
        var card = new Card();
        card.setId(1L);
        cardService.saveCard(card);
        var cardId = card.getId();

        var updatedCard = new Card();
        updatedCard.setId(cardId);
        updatedCard.setDescription("desc");
        updatedCard.setTitle("title");

        cardService.update(updatedCard, cardId);
        var foundCard = cardService.getCardById(cardId);
        assertEquals(updatedCard.getDescription(), foundCard.getDescription());
        assertEquals(updatedCard.getTitle(), foundCard.getTitle());
    }

    @Test
    void updateCardWithWrongId() {
        var card = new Card();
        card.setId(1L);
        cardService.saveCard(card);
        var cardId = card.getId();

        var updatedCard = new Card();
        updatedCard.setId(2L);
        updatedCard.setDescription("desc");
        updatedCard.setTitle("title");

        assertThrows(IllegalArgumentException.class, () -> cardService.update(updatedCard, cardId));
    }

    @Test
    void testEverythingWithNull() {
        assertThrows(IllegalArgumentException.class, () -> cardService.saveCard(null));
        assertThrows(IllegalArgumentException.class, () -> cardService.update(null, 5L));
    }
}
