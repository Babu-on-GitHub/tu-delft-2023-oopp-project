package server.api;

import commons.Card;
import commons.CardList;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.CardListRepository;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/list")
public class CardListController {

    static Logger log = Logger.getLogger(CardListController.class.getName());

    private CardListRepository cardListRepository;

    public CardListController(CardListRepository cardListRepository) {
        this.cardListRepository = cardListRepository;
    }

    @GetMapping(path = {"", "/"})
    public List<CardList> getAll() {
        return cardListRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardList> getById(@PathVariable("id") long id) {
        log.info("Getting card list: " + id);
        if (id < 0 || !cardListRepository.existsById(id)) {
            log.warning("Trying to get non existing card list");
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(cardListRepository.findById(id).get());
    }

    @PostMapping(path = "/add/{id}")
    public ResponseEntity<Card> add(@RequestBody Card card, @PathVariable("id") long listId) {
        if (card == null || !cardListRepository.existsById(listId)) {
            return ResponseEntity.badRequest().build();
        }
        log.info("Adding card: " + card.getId() + " to list: " + listId);


        var listOptional = cardListRepository.findById(listId);
        if (listOptional.isEmpty()) {
            log.warning("Trying to add card into non-existent list");
            return ResponseEntity.badRequest().build();
        }

        var list = listOptional.get();
        card.sync();
        list.getCards().add(card);
        list.sync();
        cardListRepository.save(list);

        // extract the newly assigned id
        var res = list.getCards().get(list.getCards().size() - 1);

        log.info("New card id: " + res.getId());

        return ResponseEntity.ok(res);
    }

    @DeleteMapping(path = "/delete/{cardId}/from/{listId}")
    public ResponseEntity<Boolean> remove(@PathVariable("cardId") long cardId, @PathVariable("listId") long listId) {
        log.info("Deleting card: " + cardId + " from list: " + listId);
        if (!cardListRepository.existsById(listId)) {
            log.warning("Trying to delete card from non existing card list");
            return ResponseEntity.badRequest().build();
        }
        var listOpt = cardListRepository.findById(listId);
        if (listOpt.isEmpty()) {
            log.warning("Trying to delete card from non existing card list");
            return ResponseEntity.badRequest().build();
        }

        var list = listOpt.get();
        var cards = list.getCards();
        var cardOpt = cards.stream().filter(c -> c.getId() == cardId).findFirst();
        if (cardOpt.isEmpty()) {
            log.warning("Trying to delete non existing card");
            return ResponseEntity.badRequest().build();
        }
        cards.remove(cardOpt.get());
        list.sync();
        cardListRepository.save(list);

        return ResponseEntity.ok(true);
    }

    @PutMapping(path = "/update/{id}")
    public ResponseEntity<CardList> update(@RequestBody CardList cardList, @PathVariable("id") long id) {
        log.info("Updating card list: " + id);
        if (cardList == null || !cardListRepository.existsById(id)) {
            log.warning("Trying to update non existing card list");
            return ResponseEntity.badRequest().build();
        }

        if (cardList.getId() != id) {
            log.warning("CardLists update ids do not match");
        }

        var stored = cardListRepository.findById(id);
        if (stored.isEmpty()) {
            log.warning("Problems during card list update");
            return ResponseEntity.badRequest().build();
        }

        var storedList = stored.get();
        storedList.sync();

        storedList.assign(cardList);
        var saved = cardListRepository.save(storedList);
        return ResponseEntity.ok(saved);
    }
}
