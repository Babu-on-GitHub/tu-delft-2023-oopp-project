package server.api;

import commons.CardList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class CardListControllerTest {

    private TestCardListRepository repository;

    private CardListController controller;

    @BeforeEach
    public void setup() {
        repository = new TestCardListRepository();
        controller = new CardListController(repository);
    }

    @Test
    public void cannotAddNull(){
        var test = controller.add(null);
        assertEquals(BAD_REQUEST, test.getStatusCode());
    }

    @Test
    public void addCardListTest1(){
        var cardList = new CardList();
        var test = controller.add(cardList);
        assertEquals(HttpStatus.OK, test.getStatusCode());

        var all = controller.getAll();
        assertEquals(1, all.size());
        assertEquals(cardList, all.get(0));
    }

    @Test
    public void addCardListTest2(){
        var first = new CardList();
        first.setTitle("test");
        first.setCards(new ArrayList<>());
        var test = controller.add(first);
        assertEquals(HttpStatus.OK, test.getStatusCode());

        var second = new CardList();
        second.setTitle("test2");
        second.setCards(new ArrayList<>());
        var test2 = controller.add(second);
        assertEquals(HttpStatus.OK, test2.getStatusCode());

        var all = controller.getAll();
        assertEquals(2, all.size());
        assertEquals(first, all.get(0));
        assertEquals(second, all.get(1));
    }

    @Test
    public void getByIdTest(){
        var first = new CardList();
        first.setTitle("test");
        first.setCards(new ArrayList<>());
        var test = controller.add(first);
        assertEquals(HttpStatus.OK, test.getStatusCode());

        var second = new CardList();
        second.setTitle("test2");
        second.setCards(new ArrayList<>());
        var test2 = controller.add(second);
        assertEquals(HttpStatus.OK, test2.getStatusCode());

        var all = controller.getAll();
        assertEquals(2, all.size());
        assertEquals(first, all.get(0));
        assertEquals(second, all.get(1));
    }
}
