package server.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.database.BoardRepository;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class BoardControllerTest {

    private Random random;
    private TestBoardRepository repository;

    private BoardController controller;

    @BeforeEach
    public void setup() {
        random = new Random();
        repository = new TestBoardRepository();
        controller = new BoardController(repository);
    }

    @Test
    public void cannotAddNull(){
        var test = controller.add(null);
        assertEquals(BAD_REQUEST, test.getStatusCode());
    }
}
