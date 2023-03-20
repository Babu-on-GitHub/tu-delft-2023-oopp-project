package server.api;

import commons.CardList;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import server.database.CardListRepository;

import java.util.List;

@RestController
@RequestMapping("/api/list")
public class CardListController {

    private CardListRepository cardListRepository;

    public CardListController(CardListRepository cardListRepository){
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

    @PostMapping(path = "/add")
    public ResponseEntity<CardList> add(@RequestBody CardList cardList) {
        if(cardList == null){
            return ResponseEntity.badRequest().build();
        }
        CardList saved = cardListRepository.save(cardList);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping(path = "/remove/{id}")
    public ResponseEntity<Boolean> remove(@PathVariable("id") long id) {
        if(!cardListRepository.existsById(id)){
            return ResponseEntity.badRequest().build();
        }
        cardListRepository.deleteById(id);
        return ResponseEntity.ok(true);
    }

    @PutMapping(path = "/update/{id}")
    public ResponseEntity<CardList> update(@RequestBody CardList cardList, @PathVariable("id") long id) {
        if(cardList == null || !cardListRepository.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        var saved = cardListRepository.save(cardList);
        return ResponseEntity.ok(saved);
    }

    @MessageMapping("/cardlist/update/{id}")
    @SendTo("/topic/cardlist/{id}")
    public CardList updateCardList(CardList cardList, @DestinationVariable long id) {
        return update(cardList, id).getBody();
    }

    @MessageMapping("/cardlist/remove/{id}")
    @SendTo("/topic/cardlist/{id}")
    public CardList removeCardList(@DestinationVariable long id) {
        remove(id);
        return null;
    }
}
