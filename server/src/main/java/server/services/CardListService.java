package server.services;

import commons.CardList;
import org.springframework.stereotype.Service;
import server.database.CardListRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CardListService {

    private CardListRepository cardListRepository;

    public CardListService(CardListRepository cardListRepository) {
        this.cardListRepository = cardListRepository;
    }

    public List<CardList> getAllCardLists() {
        return cardListRepository.findAll();
    }

    public Optional<CardList> getCardListById(long id) {
        return cardListRepository.findById(id);
    }

    public CardList addCardList(CardList cardList) {
        return cardListRepository.save(cardList);
    }

    public void removeCardListById(long id) {
        cardListRepository.deleteById(id);
    }
}

