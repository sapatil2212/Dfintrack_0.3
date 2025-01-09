package zenith_expense_tracker_nov_v1.utility;



import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class OTPGenerator {
    private static final int OTP_LENGTH = 6;

    public String generateOtp() {
        SecureRandom random = new SecureRandom();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }
}
