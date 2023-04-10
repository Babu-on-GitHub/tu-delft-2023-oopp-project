package server.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordValidationTest {

    private PasswordValidationService passServ;

    @BeforeEach
    public void setup(){
        passServ =new PasswordValidationService("parola123");
    }

    @Test
    public void passwordTest(){
        String goodPass = "parola123";
        String badPass1 = "parola1234";
        String badPass2 = "";
        String badPass3 = null;
        assertTrue(passServ.validatePassword(goodPass));
        assertFalse(passServ.validatePassword(badPass1));
        assertFalse(passServ.validatePassword(badPass2));
        assertFalse(passServ.validatePassword(badPass3));

    }
}
