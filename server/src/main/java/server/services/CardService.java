package server.services;

import commons.Card;
import org.springframework.stereotype.Service;
import server.database.CardRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CardService {

    private CardRepository cardRepository;

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public List<Card> getAllCards() {
        return cardRepository.findAll();
    }

    public Optional<Card> getCardById(long id) {
        return cardRepository.findById(id);
    }

    public Card addCard(Card card) {
        return cardRepository.save(card);
    }

    public void removeCardById(long id) {
        cardRepository.deleteById(id);
    }

}
