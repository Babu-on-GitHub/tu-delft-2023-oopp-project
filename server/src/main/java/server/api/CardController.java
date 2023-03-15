package server.api;

import commons.Card;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.CardRepository;

import java.util.List;

@RestController
@RequestMapping("/api/card")
public class CardController {

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
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(cardRepository.findById(id).get());
    }

    @PostMapping(path = "/add")
    public ResponseEntity<Card> add(@RequestBody Card card) {
        if(card == null){
            return ResponseEntity.badRequest().build();
        }
        Card saved = cardRepository.save(card);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping(path = "/remove/{id}")
    public ResponseEntity<Boolean> remove(@PathVariable("id") long id) {
        if(!cardRepository.existsById(id)){
            return ResponseEntity.badRequest().build();
        }
        cardRepository.deleteById(id);
        return ResponseEntity.ok(true);
    }

    @PutMapping(path = "/update/{id}")
    public ResponseEntity<Card> update(@RequestBody Card card, @PathVariable("id") long id) {
        if(card == null || !cardRepository.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        var saved = cardRepository.save(card);
        return ResponseEntity.ok(saved);
    }
}
