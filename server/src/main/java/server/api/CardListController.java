package server.api;

import commons.Card;
import commons.CardList;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.CardListRepository;
import server.database.CardRepository;

import java.util.List;

@RestController
@RequestMapping("/api/list")
public class CardListController {

    private CardListRepository cardListRepository;

    public CardListController(CardListRepository cardListRepository, CardRepository cardRepository){
        this.cardListRepository = cardListRepository;
    }

    @GetMapping(path = { "", "/" })
    public List<CardList> getAll() {
        return cardListRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardList> getById(@PathVariable("id") long id) {
        if (id < 0 || !cardListRepository.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(cardListRepository.findById(id).get());
    }

    @PostMapping(path = { "", "/" })
    public ResponseEntity<CardList> add(@RequestBody CardList cardList) {
        if(cardList == null){
            return ResponseEntity.badRequest().build();
        }
        CardList saved = cardListRepository.save(cardList);
        return ResponseEntity.ok(saved);
    }
}
