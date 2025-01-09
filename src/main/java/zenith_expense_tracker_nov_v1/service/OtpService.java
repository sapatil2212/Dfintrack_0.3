package zenith_expense_tracker_nov_v1.service;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class OtpService {

    private static final int OTP_LENGTH = 6;
    private final Map<String, String> otpStore = new HashMap<>();
    private final Map<String, LocalDateTime> otpExpiry = new HashMap<>();

    public String generateOtp() {
        SecureRandom random = new SecureRandom();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }

    public void storeOtp(String email, String otp) {
        otpStore.put(email, otp);
        otpExpiry.put(email, LocalDateTime.now().plusMinutes(5)); // OTP expires in 5 minutes
    }

    public boolean verifyOtp(String email, String otp) {
        String storedOtp = otpStore.get(email);
        LocalDateTime expiry = otpExpiry.get(email);

        if (storedOtp == null || expiry == null || expiry.isBefore(LocalDateTime.now())) {
            // OTP expired or not found
            otpStore.remove(email);
            otpExpiry.remove(email);
            return false;
        }

        // OTP verification
        if (!storedOtp.equals(otp)) {
            return false; // OTP mismatch
        }

        // If OTP matches and is valid, remove it from store
        otpStore.remove(email);
        otpExpiry.remove(email);
        return true;
    }
}
