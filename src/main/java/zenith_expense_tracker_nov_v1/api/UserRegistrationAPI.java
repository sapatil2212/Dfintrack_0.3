package zenith_expense_tracker_nov_v1.api;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zenith_expense_tracker_nov_v1.dto.UserDTO;
import zenith_expense_tracker_nov_v1.exception.InvalidSecurityKeyException;
import zenith_expense_tracker_nov_v1.service.UserRegistrationService;
import zenith_expense_tracker_nov_v1.exception.ResourceNotFoundException;
import zenith_expense_tracker_nov_v1.exception.UserAlreadyExistsException;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/users")
public class UserRegistrationAPI {

    @Autowired
    private UserRegistrationService userRegistrationService;

    @PostMapping("/caretaker")
    public ResponseEntity<Object> registerUser(@RequestBody @Valid UserDTO userDTO) {
        try {
            UserDTO registeredUser = userRegistrationService.registerUserWithProperty(userDTO);
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
        } catch (UserAlreadyExistsException | IllegalArgumentException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        }catch (InvalidSecurityKeyException e){
            return new ResponseEntity<>(new UserAPI.ErrorResponse(e.getMessage()),HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse("An unexpected error occurred"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Custom error response
    public static class ErrorResponse {
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
