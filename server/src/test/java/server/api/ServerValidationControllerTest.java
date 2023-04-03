package server.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.services.PasswordValidationService;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServerValidationControllerTest {

    private ServerValidationController controller;

    @BeforeEach
    void setUp() {
        controller = new ServerValidationController( new PasswordValidationService("parola123"));
    }

    @Test
    void testValidate() {
        ResponseEntity<String> response = controller.validate();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Running", response.getBody());
    }

    @Test
    void testValidateAdmin(){
        ResponseEntity<String> response = controller.validateAdmin("parola123");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Valid", response.getBody());
    }

}
