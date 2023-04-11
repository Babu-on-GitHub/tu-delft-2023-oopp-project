package server.api;

import commons.Card;
import commons.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import server.services.CardPollingService;
import server.services.CardService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/card")
public class CardController {

    static Logger log = Logger.getLogger(CardController.class.getName());

    private CardService cardService;

    private CardPollingService cardPollingService;

    public CardController(CardService cardService, CardPollingService cardPollingService) {
        this.cardService = cardService;
        this.cardPollingService = cardPollingService;
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
        throw new UnsupportedOperationException("The operation is not supported");
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<Boolean> remove(@PathVariable("id") long id) {
        throw new UnsupportedOperationException("The operation is not supported");
    }

    private Map<Object, Consumer<Card>> listeners = new HashMap<>();

    @PutMapping(path = "/update/{id}")
    public ResponseEntity<Card> update(@RequestBody Card card, @PathVariable("id") long id) {
        log.info("update(" + id + ")");
        try {
            var saved = cardService.update(card, id);
            listeners.forEach((k,v)->{v.accept(saved);});
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
            var card = cardService.getCardById(id);
            listeners.forEach((k,v)->{
                v.accept(card);
            });
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

    @GetMapping(path = "/poll")
    public DeferredResult<ResponseEntity<Card>> poll(){
        var nothing = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        var r = new DeferredResult<ResponseEntity<Card>>(1000L,nothing);
        Object key = new Object();
        listeners.put(key,x -> {
            r.setResult(ResponseEntity.ok(x));
            System.out.println("put a listener ");
        });
        r.onCompletion(() -> {
            listeners.remove(key);
            System.out.println("removed the listener ");
        });

        return r;
    }
}
