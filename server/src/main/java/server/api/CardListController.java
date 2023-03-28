package server.api;

import commons.Card;
import commons.CardList;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.services.CardListService;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/list")
public class CardListController {

    static Logger log = Logger.getLogger(CardListController.class.getName());

    private CardListService cardListService;

    public CardListController(CardListService cardListService) {
        this.cardListService = cardListService;
    }

    @GetMapping(path = {"", "/"})
    public List<CardList> getAll() {
        return cardListService.getAllCardLists();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardList> getById(@PathVariable("id") long id) {
        log.info("Getting card list: " + id);
        try {
            cardListService.getCardListById(id);
            return ResponseEntity.ok(cardListService.getCardListById(id));
        } catch (IllegalArgumentException e) {
            log.warning(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(path = "/add/{id}")
    public ResponseEntity<Card> add(@RequestBody Card card, @PathVariable("id") long listId) {
        log.info("Adding card: " + card.getId() + " to list: " + listId);
        try {
            var res = cardListService.addCard(card, listId);
            log.info("New card id: " + res.getId());
            return ResponseEntity.ok(res);
        } catch (IllegalArgumentException e) {
            log.warning(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping(path = "/delete/{cardId}/from/{listId}")
    public ResponseEntity<Boolean> remove(@PathVariable("cardId") long cardId, @PathVariable("listId") long listId) {
        log.info("Deleting card: " + cardId + " from list: " + listId);
        try {
            cardListService.removeCard(cardId, listId);
            return ResponseEntity.ok(true);
        } catch (IllegalArgumentException e) {
            log.warning(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(path = "/update/{id}")
    public ResponseEntity<CardList> update(@RequestBody CardList cardList, @PathVariable("id") long id) {
        log.info("Updating card list: " + id);
        try {
            var saved = cardListService.update(cardList, id);
            return ResponseEntity.ok(saved);

        } catch (IllegalArgumentException e) {
            log.warning(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(path = "/updateTitle/{id}")
    public ResponseEntity<String> updateTitle(@RequestBody String title, @PathVariable("id") long id) {
        log.info("Updating card list title: " + id);
        try {
            var saved = cardListService.updateTitle(id, title);
            return ResponseEntity.ok(saved);

        } catch (IllegalArgumentException e) {
            log.warning(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
