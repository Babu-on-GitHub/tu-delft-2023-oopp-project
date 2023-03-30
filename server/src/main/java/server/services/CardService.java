package server.services;

import commons.Card;
import commons.Tag;
import commons.Task;
import org.springframework.stereotype.Service;
import server.database.CardRepository;

import java.util.ArrayList;
import java.util.HashSet;
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
        return this.saveCard(card);
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
    public String updateDescription(long id, String description) {
        Card card = this.getCardById(id);
        card.setDescription(description);
        card.sync();
        this.saveCard(card);

        return description;
    }
    public List<Task> updateSubtasks(long id, List<Task> subtasks) {
        Card card = this.getCardById(id);
        card.setSubTasks((ArrayList<Task>) subtasks);
        card.sync();
        this.saveCard(card);

        return subtasks;
    }
    public Set<Tag> updateTags(long id, Set<Tag> tags) {
        Card card = this.getCardById(id);
        card.setTags((HashSet<Tag>) tags);
        card.sync();
        this.saveCard(card);

        return tags;
    }
}
