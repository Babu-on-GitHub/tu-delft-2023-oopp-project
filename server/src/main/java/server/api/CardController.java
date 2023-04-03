package server.api;

import commons.Card;
import commons.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.services.CardService;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/card")
public class CardController {

    static Logger log = Logger.getLogger(CardController.class.getName());

    private CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping(path = {"", "/"})
    public List<Card> getAll() {
        return cardService.getAllCards();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Card> getById(@PathVariable("id") long id) {
        log.info("getById(" + id + ")");
        try {
            var card = cardService.getCardById(id);
            return ResponseEntity.ok(card);
        } catch (IllegalArgumentException e) {
            log.warning(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(path = "/add")
    public ResponseEntity<Card> add(@RequestBody Card card) {
        throw new UnsupportedOperationException();
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<Boolean> remove(@PathVariable("id") long id) {
        throw new UnsupportedOperationException();
    }

    @PutMapping(path = "/update/{id}")
    public ResponseEntity<Card> update(@RequestBody Card card, @PathVariable("id") long id) {
        log.info("update(" + id + ")");
        try {
            var saved = cardService.update(card, id);
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException e) {
            log.warning(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(path = "/updateTitle/{id}")
    public ResponseEntity<String> updateTitle(@RequestBody String title, @PathVariable("id") long id) {
        log.info("updateTitle(" + title + ", " + id + ")");
        try {
            var saved = cardService.updateTitle(id, title);
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException e) {
            log.warning(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(path = "/assignTag/{id}")
    public ResponseEntity<Tag> addTag(@RequestBody Tag tag, @PathVariable("id") long cardId) {
        log.info("addTag(" + tag + ", " + cardId + ")");
        try {
            var saved = cardService.addTag(cardId, tag);
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException e) {
            log.warning(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping(path = "/deleteTag/{tagId}/from/{cardId}")
    public ResponseEntity<Boolean> removeTag(@PathVariable("tagId") long tagId, @PathVariable("cardId") long cardId) {
        log.info("removeTag(" + tagId + ", " + cardId + ")");
        try {
            cardService.removeTag(cardId, tagId);
            return ResponseEntity.ok(true);
        } catch (IllegalArgumentException e) {
            log.warning(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
