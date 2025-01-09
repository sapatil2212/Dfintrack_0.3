package zenith_expense_tracker_nov_v1.api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
        import zenith_expense_tracker_nov_v1.dto.AccountType;
import zenith_expense_tracker_nov_v1.dto.UserDTO;
import zenith_expense_tracker_nov_v1.service.ManageUserService;

import java.util.List;

//Class to manage (CRUD) on USER including ADMIN (Endpoint)
@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/users")
public class ManageUsersController {

    @Autowired
    private ManageUserService manageUserService;

    // Fetch all users (Admin and User)
    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = manageUserService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // Fetch all users with account type USER
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsersByAccountTypeUser() {
        List<UserDTO> users = manageUserService.getUsersByAccountType(AccountType.USER);
        return ResponseEntity.ok(users);
    }

    // Fetch all users with account type ADMIN (excluding password)
    @GetMapping("/admins")
    public ResponseEntity<List<UserDTO>> getAllUsersByAccountTypeAdmin() {
        List<UserDTO> admins = manageUserService.getAdminsWithoutPassword();
        return ResponseEntity.ok(admins);
    }

    // Create a new user
    @PostMapping("/create")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        UserDTO createdUser = manageUserService.createUser(userDTO);
        return ResponseEntity.ok(createdUser);
    }

    // Update an existing user
    @PutMapping("/update/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = manageUserService.updateUser(id, userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    // Delete a user by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        manageUserService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully.");
    }
}
