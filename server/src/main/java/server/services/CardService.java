package server.services;

import commons.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.database.CardRepository;

import java.util.List;

@Service
public class CardService {
    private CardRepository cardRepository;

    @Autowired
    SynchronizationService synchronizationService;

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public List<Card> getAllCards() {
        return cardRepository.findAll();
    }

    public Card getCardById(long id) {
        if (id < 0 || cardRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("Trying to get non existing card");
        }
        return cardRepository.findById(id).orElse(null);
    }

    public Card saveCard(Card card) {
        if (card == null) {
            throw new IllegalArgumentException("Card list cannot be null");
        }

        synchronizationService.addCardToUpdate(card.getId());
        return cardRepository.save(card);
    }

    public Card update(Card card, long id) {
        if (card == null) {
            throw new IllegalArgumentException("Trying to update non existing card");
        }
        try {
            Card cardOptional = this.getCardById(id);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }

        if (card.getId() != id) {
            throw new IllegalArgumentException("Ids are inconsistent in card update");
        }

        card.sync();
        return this.saveCard(card);
    }

    public void removeCardById(long id) {
        cardRepository.deleteById(id);
    }

}
