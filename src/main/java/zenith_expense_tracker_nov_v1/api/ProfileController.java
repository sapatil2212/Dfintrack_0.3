package zenith_expense_tracker_nov_v1.api;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import zenith_expense_tracker_nov_v1.dto.UpdateUserDTO;
import zenith_expense_tracker_nov_v1.entity.User;
import zenith_expense_tracker_nov_v1.service.ProfileService;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping
    public ResponseEntity<Object> getProfile() {
        String email = getCurrentUserEmail();
        User user = profileService.getUser(email);  // Ensure this method fetches the property correctly
        return ResponseEntity.ok(user.toDTO()); // Or return the user entity directly if preferred
    }
    @PutMapping("/update/username")
    public ResponseEntity<String> updateUsername(@RequestBody @Valid UpdateUserDTO updateUserDTO) {
        String email = getCurrentUserEmail();

        try {
            profileService.updateUsername(email, updateUserDTO.getUsername());
            return ResponseEntity.ok("Username updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update/password")
    public ResponseEntity<String> updatePassword(@RequestBody @Valid UpdateUserDTO updateUserDTO) {
        String email = getCurrentUserEmail();

        try {
            profileService.updatePassword(email, updateUserDTO.getOldPassword(), updateUserDTO.getNewPassword());
            return ResponseEntity.ok("Password updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private String getCurrentUserEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            return (String) principal;
        }
        throw new IllegalStateException("Unexpected principal type");
    }
}
