package server.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("/api/status")
public class ServerValidationController {
    @GetMapping(path = {"", "/"})
    public ResponseEntity<String> validate() {
        return ResponseEntity.ok("Running");
    }
}
