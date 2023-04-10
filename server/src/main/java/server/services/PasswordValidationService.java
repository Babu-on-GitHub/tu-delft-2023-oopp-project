package server.services;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Logger;

@Service
public class PasswordValidationService {

    private static final Logger log = Logger.getLogger(PasswordValidationService.class.getName());

    private String serverPassword;
    private String filePath = "src/main/resources/password.txt";

    public PasswordValidationService() {
        File file = new File(filePath);
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
            serverPassword = scanner.next();
        } catch (FileNotFoundException e) {
            log.warning("Could not read file when creating passwordValidationService");
        }
    }

    public PasswordValidationService(String serverPassword) {
        this.serverPassword = serverPassword;
    }

    public boolean validatePassword(String password) {
        if (serverPassword == null) {
            log.warning("Tried to validate password but saved password is null");
            return false;
        }
        return serverPassword.equals(password);
    }

}
