package server.api;

import commons.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class CardControllerTest {

    private TestCardRepository repository;

    private CardController controller;

    @BeforeEach
    public void setup() {
        repository = new TestCardRepository();
        controller = new CardController(repository);
    }

    @Test
    public void cannotAddNull(){
        var test = controller.add(null);
        assertEquals(BAD_REQUEST, test.getStatusCode());
    }

    @Test
    public void addCardTest1(){
        var card = new Card();
        var test = controller.add(card);
        assertEquals(HttpStatus.OK, test.getStatusCode());

        var all = controller.getAll();
        assertEquals(1, all.size());
        assertEquals(card, all.get(0));
    }

    @Test
    public void addCardTest2(){
        var first = new Card();
        first.setTitle("test");
        first.setDescription("test");
        var test = controller.add(first);
        assertEquals(HttpStatus.OK, test.getStatusCode());

        var second = new Card();
        second.setTitle("test2");
        second.setDescription("test2");
        var test2 = controller.add(second);
        assertEquals(HttpStatus.OK, test2.getStatusCode());

        var all = controller.getAll();
        assertEquals(2, all.size());
        assertEquals(first, all.get(0));
        assertEquals(second, all.get(1));
    }

    @Test
    public void findCardByIdTest() {
        var first = new Card();
        first.setTitle("test");
        first.setDescription("test");
        var test = controller.add(first);
        assertEquals(HttpStatus.OK, test.getStatusCode());

        var second = new Card();
        second.setTitle("test2");
        second.setDescription("test2");
        var test2 = controller.add(second);
        assertEquals(HttpStatus.OK, test2.getStatusCode());

        var all = controller.getAll();
        assertEquals(2, all.size());
        assertEquals(first, all.get(0));
        assertEquals(second, all.get(1));
    }

}
