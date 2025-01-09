package zenith_expense_tracker_nov_v1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zenith_expense_tracker_nov_v1.dto.AccountType;
import zenith_expense_tracker_nov_v1.dto.UserDTO;
import zenith_expense_tracker_nov_v1.entity.User;
import zenith_expense_tracker_nov_v1.exception.UserNotFoundException;
import zenith_expense_tracker_nov_v1.exception.InvalidSecurityKeyException;
import zenith_expense_tracker_nov_v1.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CaretakerService {

    private static final String PSC = "09876"; // Permanent Security Key

    @Autowired
    private UserRepository userRepository;

    // Get all users (for admin)
    public Iterable<UserDTO> getAllUsers() {
        Iterable<User> users = userRepository.findAll();
        return StreamSupport.stream(users.spliterator(), false)
                .map(UserDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // Get user by ID
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
        return UserDTO.fromEntity(user);
    }

    // Create new user
    public UserDTO createUser(UserDTO userDTO) {
        User user = userDTO.toEntity();
        user = userRepository.save(user);
        return UserDTO.fromEntity(user);
    }

    // Update user (Admin only)
    public UserDTO updateUser(Long id, UserDTO userDTO, String securityKey) {
        if (!PSC.equals(securityKey)) {
            throw new InvalidSecurityKeyException("Invalid Permanent Security Key");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));

        // Update the fields of user
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());  // Password should already be hashed
        user.setAccountType(userDTO.getAccountType());
        user.setSecurityKey(userDTO.getSecurityKey());

        user = userRepository.save(user);
        return UserDTO.fromEntity(user);
    }

    // Delete user (Admin only)
    public void deleteUser(Long id, String securityKey) {
        if (!PSC.equals(securityKey)) {
            throw new InvalidSecurityKeyException("Invalid Permanent Security Key");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));

        userRepository.delete(user);
    }
    public Iterable<UserDTO> getAllUsersByRole(String role) { List<User> users = userRepository.findByAccountType(AccountType.valueOf(role)); return users.stream() .map(UserDTO::fromEntity) .collect(Collectors.toList()); }
}
