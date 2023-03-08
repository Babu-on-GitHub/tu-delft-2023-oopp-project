package server.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.database.CardRepository;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class CardControllerTest {

    private Random random;
    private TestCardRepository repository;

    private CardController controller;

    @BeforeEach
    public void setup() {
        random = new Random();
        repository = new TestCardRepository();
        controller = new CardController(repository);
    }

    @Test
    public void cannotAddNull(){
        var test = controller.add(null);
        assertEquals(BAD_REQUEST, test.getStatusCode());
    }

}
