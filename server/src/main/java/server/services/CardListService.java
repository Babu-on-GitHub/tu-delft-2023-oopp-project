package server.services;

import commons.Card;
import commons.CardList;
import org.springframework.stereotype.Service;
import server.api.CardListController;
import server.database.CardListRepository;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class CardListService {

    private static final Logger log = Logger.getLogger(CardListController.class.getName());

    private final CardListRepository cardListRepository;
    private final SynchronizationService synchronizationService;

    public CardListService(CardListRepository cardListRepository, SynchronizationService syncronizationService) {
        this.cardListRepository = cardListRepository;
        this.synchronizationService = syncronizationService;
    }

    public List<CardList> getAllCardLists() {
        return cardListRepository.findAll();
    }

    public CardList getCardListById(long id) {
        if (id < 0 || cardListRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("Trying to get non-existing card list");
        }
        return cardListRepository.findById(id).orElse(null);
    }

    public CardList saveCardList(CardList cardList) {
        if (cardList == null) {
            throw new IllegalArgumentException("Card list cannot be null");
        }

        synchronizationService.addListToUpdate(cardList.getId());
        return cardListRepository.save(cardList);
    }

    public Card addCard(Card card, long listId) {
        if (card == null) {
            throw new IllegalArgumentException("Trying to add a null cad");
        }
        try {
            var listOptional = this.getCardListById(listId);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Trying to add card into non-existent list");
        }

        var list = this.getCardListById(listId);
        card.sync();
        list.getCards().add(card);
        list.sync();
        this.saveCardList(list);

        // extract the newly assigned id
        var res = list.getCards().get(list.getCards().size() - 1);

        log.info("New card id: " + res.getId());

        return res;
    }

    public void removeCard(long cardId, long listId) {
        if (this.getCardListById(listId) == null) {
            throw new IllegalArgumentException("Trying to delete card from non existing card list");
        }

        var list = getCardListById(listId);
        var cards = list.getCards();
        var cardOpt = cards.stream().filter(c -> c.getId() == cardId).findFirst();
        if (cardOpt.isEmpty()) {
            throw new IllegalArgumentException("Trying to delete non existing card");
        }
        cards.remove(cardOpt.get());
        list.sync();
        this.saveCardList(list);
    }

    public CardList update(CardList cardList, long id) {
        if (cardList == null) {
            throw new IllegalArgumentException("Trying to update non existing card list");
        }
        try {
            var cardListOptional = this.getCardListById(id);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        if (cardList.getId() != id) {
            throw new IllegalArgumentException("CardLists update ids do not match");
        }

        var stored = this.getCardListById(id);
        if (stored == null) {
            throw new IllegalArgumentException("Problems during card list update");
        }

        cardList.sync();
        return this.saveCardList(cardList);
    }

    public String updateTitle(long id, String title) {
        if (title == null) {
            throw new IllegalArgumentException("Trying to update card list title with null");
        }

        Optional<CardList> cardListOptional;
        try {
            cardListOptional = Optional.ofNullable(getCardListById(id));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }

        var cardList = cardListOptional.get();
        cardList.setTitle(title);
        cardList.sync();
        this.saveCardList(cardList);

        return title;
    }
}

