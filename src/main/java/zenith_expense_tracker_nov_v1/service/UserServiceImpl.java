package zenith_expense_tracker_nov_v1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import zenith_expense_tracker_nov_v1.dto.UserDTO;
import zenith_expense_tracker_nov_v1.dto.LoginDTO;
import zenith_expense_tracker_nov_v1.entity.User;
import zenith_expense_tracker_nov_v1.exception.UserAlreadyExistsException;
import zenith_expense_tracker_nov_v1.exception.UserNotFoundException;
import zenith_expense_tracker_nov_v1.exception.InvalidSecurityKeyException;
import zenith_expense_tracker_nov_v1.jwt.AuthenticationResponse;
import zenith_expense_tracker_nov_v1.jwt.CustomUserDetails;
import zenith_expense_tracker_nov_v1.jwt.JwtHelper;
import zenith_expense_tracker_nov_v1.jwt.RefreshToken;
import zenith_expense_tracker_nov_v1.repository.UserRepository;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service(value = "userService")
public class UserServiceImpl implements UserService {

    String PSC = "09876";
    private static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtHelper jwtHelper;
    @Override
    public UserDTO registerUser(UserDTO userDTO) {

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

        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        User user = userDTO.toEntity();
        userRepository.save(user);

        return user.toDTO();
    }

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Override
    public AuthenticationResponse loginUser(LoginDTO loginDTO) {
        // Validate email format
        if (!isValidEmail(loginDTO.getEmail())) {
            throw new IllegalArgumentException("Email is not valid");
        }

        // Check if user exists
        User user = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User with email " + loginDTO.getEmail() + " not registered"));

        // Check if password matches
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Wrong password entered");
        }

        if (!PSC.equals(loginDTO.getSecurityKey())) {
            throw new InvalidSecurityKeyException("Invalid Security Key");
        }

        // Generate tokens
        CustomUserDetails userDetails = new CustomUserDetails(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getPassword(),
                user.getAccountType(),
                user.getSecurityKey(),
                new ArrayList<>(),
                user.getProperty() != null ? user.getProperty().getId() : null,  // Include propertyId
                user.getProperty() != null ? user.getProperty().getName() : null  // Include propertyName
        );

        String accessToken = jwtHelper.generateToken(userDetails);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());

        return new AuthenticationResponse(accessToken, refreshToken.getToken());
    }

   /* @Override
    public AuthenticationResponse loginUser(LoginDTO loginDTO) {

        // Validate email format
        if (!isValidEmail(loginDTO.getEmail())) {
            throw new IllegalArgumentException("Email is not valid");
        }

        // Check if user exists
        User user = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User with email " + loginDTO.getEmail() + " not registered"));

        // Check if password matches
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Wrong password entered");
        }


        if (!PSC.equals(loginDTO.getSecurityKey())) {
            throw new InvalidSecurityKeyException("Invalid Security Key");
        }


        String jwtToken = jwtHelper.generateToken(new CustomUserDetails(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getPassword(),
                user.getAccountType(),
                user.getSecurityKey(),
                new ArrayList<>(),
                user.getProperty() != null ? user.getProperty().getId() : null,  // Include propertyId
                user.getProperty() != null ? user.getProperty().getName() : null  // Include propertyName
        ));

        return new AuthenticationResponse(jwtToken);
    }*/


    @Override
    public UserDTO getUserByEmail(String email) throws UserNotFoundException {
       return userRepository.findByEmail(email).orElseThrow(()-> new UserNotFoundException("USER_NOT_FOUND")).toDTO();

    }


    public boolean checkEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }


    public boolean validateSecurityKey(String securityKey) {
        return PSC.equals(securityKey);
    }
    // Helper method to validate email format
    private boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}