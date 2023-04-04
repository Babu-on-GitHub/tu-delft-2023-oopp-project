package server.services;

import commons.Card;
import commons.Tag;
import org.springframework.stereotype.Service;
import server.database.CardRepository;

import java.util.List;
import java.util.Set;

@Service
public class CardService {
    private CardRepository cardRepository;
    SynchronizationService synchronizationService;

    public CardService(CardRepository cardRepository, SynchronizationService synchronizationService) {
        this.cardRepository = cardRepository;
        this.synchronizationService = synchronizationService;
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
        var ret = saveCard(card);

        return saveCard(ret);
    }

    public void removeCardById(long id) {
        cardRepository.deleteById(id);
    }

    public String updateTitle(long id, String title) {
        Card card = this.getCardById(id);
        card.setTitle(title);
        card.sync();
        this.saveCard(card);

        return title;
    }

    public Tag addTag(long id, Tag tag) {
        Card card = this.getCardById(id);
        card.getTags().add(tag);

        card.sync();
        this.saveCard(card);

        return tag;
    }

    public void removeTag(long id, long tagId) {
        Card card = this.getCardById(id);

        Set<Tag> tags = card.getTags();
        tags.removeIf(tag -> tag.getId() == tagId);
        card.setTags(tags);

        card.sync();
        this.saveCard(card);
    }

}
