package zenith_expense_tracker_nov_v1.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zenith_expense_tracker_nov_v1.dto.ForgotPasswordDTO;
import zenith_expense_tracker_nov_v1.exception.InvalidSecurityKeyException;
import zenith_expense_tracker_nov_v1.service.ForgotPasswordService;
import zenith_expense_tracker_nov_v1.utility.ErrorInfo;

//Class to manage forgot password endpoints
@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:5173")
public class ForgotPasswordController {

    @Autowired
    private ForgotPasswordService forgotPasswordService;

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody ForgotPasswordDTO dto) {
        try {
            forgotPasswordService.sendOtp(dto.getEmail());
            return ResponseEntity.ok("OTP sent successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody ForgotPasswordDTO dto) {
        try {
            forgotPasswordService.verifyOtp(dto);
            return ResponseEntity.ok("OTP verified successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/verify-security-key")
    public ResponseEntity<?> verifySecurityKey(@RequestBody ForgotPasswordDTO dto) {
        try {
            forgotPasswordService.verifySecurityKey(dto);
            return ResponseEntity.ok("Security key validated successfully.");
        } catch (InvalidSecurityKeyException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ForgotPasswordDTO dto) {
        try {
            forgotPasswordService.resetPassword(dto);
            return ResponseEntity.ok("Password reset successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
