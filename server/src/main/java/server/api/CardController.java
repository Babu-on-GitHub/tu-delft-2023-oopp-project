package server.api;

import commons.Card;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.CardRepository;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/card")
public class CardController {

    static Logger log = Logger.getLogger(CardController.class.getName());

    private CardRepository cardRepository;

    public CardController(CardRepository cardRepository){
        this.cardRepository = cardRepository;
    }

    @GetMapping(path = { "", "/" })
    public List<Card> getAll() {
        return cardRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Card> getById(@PathVariable("id") long id) {
        if (id < 0 || !cardRepository.existsById(id)) {
            log.warning("Trying to get non existing card");
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(cardRepository.findById(id).get());
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
        if (card == null || !cardRepository.existsById(id)) {
            log.warning("Trying to update non existing card");
            return ResponseEntity.badRequest().build();
        }

        if (card.getId() != id) {
            log.warning("Ids are inconsistent in card update");
        }

        card.sync();
        var saved = cardRepository.save(card);
        return ResponseEntity.ok(saved);
    }
}
