package server.api;

import commons.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class BoardControllerTest {

    private TestBoardRepository repository;

    private BoardController controller;

    @BeforeEach
    public void setup() {
        repository = new TestBoardRepository();
        controller = new BoardController(repository);
    }

    @Test
    public void cannotAddNull(){
        var test = controller.add(null);
        assertEquals(BAD_REQUEST, test.getStatusCode());
    }

    @Test
    public void addBoardTest1(){
        var board = new Board();
        var test = controller.add(board);
        assertEquals(HttpStatus.OK, test.getStatusCode());

        var all = controller.getAll();
        assertEquals(1, all.size());
        assertEquals(board, all.get(0));
    }

    @Test
    public void addBoardTest2(){
        var first = new Board();
        first.setTitle("test");
        first.setLists(new ArrayList<>());
        var test = controller.add(first);
        assertEquals(HttpStatus.OK, test.getStatusCode());

        var second = new Board();
        second.setTitle("test2");
        second.setLists(new ArrayList<>());
        var test2 = controller.add(second);
        assertEquals(HttpStatus.OK, test2.getStatusCode());

        var all = controller.getAll();
        assertEquals(2, all.size());
        assertEquals(first, all.get(0));
        assertEquals(second, all.get(1));

        assertEquals("test", all.get(0).getTitle());
        assertEquals("test2", all.get(1).getTitle());
    }

    @Test
    public void getByIdBoardTest() {
        var first = new Board();
        first.setTitle("test");
        controller.add(first);

        var second = new Board();
        second.setTitle("test2");
        controller.add(second);

        var firstGot = controller.getById(first.getId());
        assertNotSame(firstGot.getStatusCode(), BAD_REQUEST);
        var secondGot = controller.getById(second.getId());
        assertNotSame(secondGot.getStatusCode(), BAD_REQUEST);

        assertEquals(first, firstGot.getBody());
        assertEquals(second, secondGot.getBody());
    }

    @Test
    public void removeTest(){
        var first = new Board();
        first.setTitle("test");
        controller.add(first);

        var all = controller.getAll();
        assertEquals(1, all.size());
        assertEquals(all.get(0),first);

        var removed = controller.remove(0L);
        assertEquals(HttpStatus.OK, removed.getStatusCode());

        all=controller.getAll();
        assertEquals(0, all.size());

        removed = controller.remove(123L);
        assertEquals(HttpStatus.BAD_REQUEST, removed.getStatusCode());

    }

    //TODO remake update test and test repository (old test was not representative for how method interacts with database)

//    @Test
//    public void updateTest(){
//        var first = new Board();
//        first.setTitle("test");
//        controller.add(first);
//
//        var all = controller.getAll();
//        assertEquals(1, all.size());
//        assertEquals(all.get(0),first);
//        assertEquals(all.get(0).getId(),0);
//
//        var second = new Board();
//        second.setTitle("changed title");
//
//        var updated = controller.update(second,0L);
//        assertEquals(HttpStatus.OK, updated.getStatusCode());
//
//        all = controller.getAll();
//        assertEquals(1, all.size());
//        assertEquals(all.get(0).getId(),0);
//        assertEquals(all.get(0).getTitle(),"changed title");
//
//    }


}
