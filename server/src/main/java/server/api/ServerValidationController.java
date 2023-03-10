package server.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("/api")
public class ServerValidationController {
    @GetMapping("/ServerChoice")
    public ResponseEntity<String> validate() {
        return ResponseEntity.ok("successfully connected");
    }
}
