package server.api.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.api.ServerValidationController;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServerValidationControllerTest {

    private ServerValidationController controller;

    @BeforeEach
    void setUp() {
        controller = new ServerValidationController();
    }

    @Test
    void testValidate() {
        ResponseEntity<String> response = controller.validate();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Running", response.getBody());
    }

}
