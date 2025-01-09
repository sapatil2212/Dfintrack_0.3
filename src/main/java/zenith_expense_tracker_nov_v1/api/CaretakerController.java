package zenith_expense_tracker_nov_v1.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import zenith_expense_tracker_nov_v1.dto.UserDTO;
import zenith_expense_tracker_nov_v1.service.CaretakerService;

//Class to manage (CRUD) on caretakers (Endpoint)
@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/caretaker")
public class CaretakerController {

    @Autowired
    private CaretakerService caretakerService;



    @GetMapping
    public ResponseEntity<Iterable<UserDTO>> getAllUsers() {
        Iterable<UserDTO> users = caretakerService.getAllUsersByRole("USER");
        return new ResponseEntity<>(users, HttpStatus.OK);
    }



    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO userDTO = caretakerService.getUserById(id);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        UserDTO createdUser = caretakerService.createUser(userDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
    @GetMapping("/debug")
    public ResponseEntity<String> debug(Authentication authentication) {
        return ResponseEntity.ok("User: " + authentication.getName() + ", Roles: " + authentication.getAuthorities());
    }


    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable Long id,
            @RequestBody UserDTO userDTO,
            @RequestParam String securityKey) {
        UserDTO updatedUser = caretakerService.updateUser(id, userDTO, securityKey);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id,
            @RequestParam String securityKey) {
        caretakerService.deleteUser(id, securityKey);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
