package server.api;

import commons.Board;
import commons.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import server.services.CardService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class CardControllerTest {

    private TestCardRepository repository;

    private CardController controller;

    @BeforeEach
    public void setup() {
        repository = new TestCardRepository();
        controller = new CardController(new CardService(repository));
    }
}
