package zenith_expense_tracker_nov_v1.api;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import zenith_expense_tracker_nov_v1.dto.UserDTO;
import zenith_expense_tracker_nov_v1.dto.LoginDTO;
import zenith_expense_tracker_nov_v1.entity.User;
import zenith_expense_tracker_nov_v1.exception.TokenRefreshException;
import zenith_expense_tracker_nov_v1.jwt.*;
import zenith_expense_tracker_nov_v1.repository.UserRepository;
import zenith_expense_tracker_nov_v1.service.RefreshTokenService;
import zenith_expense_tracker_nov_v1.service.UserService;
import zenith_expense_tracker_nov_v1.exception.UserAlreadyExistsException;
import zenith_expense_tracker_nov_v1.exception.UserNotFoundException;
import zenith_expense_tracker_nov_v1.exception.InvalidSecurityKeyException;


import java.util.HashMap;
import java.util.Map;


@RestController
@CrossOrigin(origins = "http://localhost:5173")
@Validated
@RequestMapping("/users")
public class UserAPI {

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private UserService userService;

    //Manages Registration for ADMIN only
    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@RequestBody @Valid UserDTO userDTO) {
        try {
            userDTO = userService.registerUser(userDTO);
            return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (UserAlreadyExistsException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.CONFLICT);
        }catch (InvalidSecurityKeyException e){
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()),HttpStatus.FORBIDDEN);
        }  catch (Exception e) {
        return new ResponseEntity<>(new ErrorResponse("An unexpected error occurred"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    //Manages Login for ADMIN/USER (BOTH)
    @PostMapping("/login")
    public ResponseEntity<Object> loginUser(@RequestBody @Valid LoginDTO loginDTO) {
        try {
            AuthenticationResponse authResponse = userService.loginUser(loginDTO);
            return new ResponseEntity<>(authResponse, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        }  catch (InvalidSecurityKeyException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.FORBIDDEN);
    }
        catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse("An unexpected error occurred"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint to check if email exists
    @GetMapping("/checkEmailExists")
    public ResponseEntity<Map<String, Boolean>> checkEmailExists(@RequestParam String email) {
        boolean exists = userService.checkEmailExists(email);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

    // Endpoint to validate the security key
    @GetMapping("/validateSecurityKey")
    public ResponseEntity<String> validateSecurityKey(@RequestParam String securityKey) {
        boolean isValid = userService.validateSecurityKey(securityKey);
        if (isValid) {
            return ResponseEntity.ok("Security key is valid.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid Security Key.");
        }
    }
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUsername)
                .map(username -> {
                    String token = jwtHelper.generateTokenFromUsername(username);
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Refresh token is not in database!"));
    }

    @GetMapping("/validateToken")
    public ResponseEntity<?> validateToken(@RequestParam String token) {
        try {
            // Validate the token using jwtHelper
            if (jwtHelper.validateToken(token)) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ErrorResponse("Token is invalid or expired."));
            }
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Token has expired. Please login again."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Invalid token."));
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

        public void setMessage(String message) {
            this.message = message;
        }
    }


}
