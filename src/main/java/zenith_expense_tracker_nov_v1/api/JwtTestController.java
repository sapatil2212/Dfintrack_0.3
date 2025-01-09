package zenith_expense_tracker_nov_v1.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/api")
public class JwtTestController {
    @PostMapping("/token")
    public ResponseEntity<String> validateToken(@RequestBody String token) {
        return ResponseEntity.ok("Token is valid");
    }
}
