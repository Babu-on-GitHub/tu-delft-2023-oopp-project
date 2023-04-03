package server.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.services.PasswordValidationService;

@RestController
@RequestMapping ("/api/status")
public class ServerValidationController {

    private PasswordValidationService passwordService;

    public ServerValidationController(PasswordValidationService passwordService){
        this.passwordService = passwordService;
    }

    @GetMapping(path = {"", "/"})
    public ResponseEntity<String> validate() {
        return ResponseEntity.ok("Running");
    }

    @PostMapping("/admin")
    public ResponseEntity<String> validateAdmin(@RequestBody String password){
        boolean valid = passwordService.validatePassword(password);
        if(valid){
            return ResponseEntity.ok("Valid");
        }
        return ResponseEntity.badRequest().build();
    }
}
