package server.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class CardListControllerTest {

    private Random random;
    private TestCardListRepository repository;

    private CardListController controller;

    @BeforeEach
    public void setup() {
        random = new Random();
        repository = new TestCardListRepository();
        controller = new CardListController(repository);
    }

    @Test
    public void cannotAddNull(){
        var test = controller.add(null);
        assertEquals(BAD_REQUEST, test.getStatusCode());
    }
}
