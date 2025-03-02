package zenith_expense_tracker_nov_v1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import zenith_expense_tracker_nov_v1.dto.AccountType;
import zenith_expense_tracker_nov_v1.dto.UserDTO;
import zenith_expense_tracker_nov_v1.entity.Property;
import zenith_expense_tracker_nov_v1.entity.User;
import zenith_expense_tracker_nov_v1.exception.ResourceNotFoundException;
import zenith_expense_tracker_nov_v1.exception.UserAlreadyExistsException;
import zenith_expense_tracker_nov_v1.exception.InvalidSecurityKeyException;
import zenith_expense_tracker_nov_v1.repository.PropertyRepository;
import zenith_expense_tracker_nov_v1.repository.UserRepository;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Service
public class UserRegistrationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String PSC = "09876"; // Permanent Security Key
    private static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*?&]{5,15}$";

    public UserDTO registerUserWithProperty(UserDTO userDTO) {
        // Validate email format
        if (!isValidEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("Email is not valid");
        }

        // Check if user already exists
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new UserAlreadyExistsException("User with email " + userDTO.getEmail() + " already exists.");
        }

        // Validate Permanent Security Key
        if (!PSC.equals(userDTO.getSecurityKey())) {
            throw new InvalidSecurityKeyException("Invalid Security Key");
        }

        // Validate password strength
        if (!isValidPassword(userDTO.getPassword())) {
            throw new IllegalArgumentException("Password must be between 5 to 15 characters long and contain both letters and numbers (and can include special characters).");
        }

        // Encode password before saving
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        User user = userDTO.toEntity();

        // Save the user to the repository
        user = userRepository.save(user);

        // Return the saved UserDTO
        return user.toDTO();
    }

    // Helper method to validate email format
    private boolean isValidEmail(String email) {
        String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$"; // Basic email regex
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // Helper method to validate password format
    private boolean isValidPassword(String password) {
        Pattern pattern = Pattern.compile(PASSWORD_REGEX);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}