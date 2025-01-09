package zenith_expense_tracker_nov_v1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import zenith_expense_tracker_nov_v1.dto.ForgotPasswordDTO;
import zenith_expense_tracker_nov_v1.entity.User;
import zenith_expense_tracker_nov_v1.exception.InvalidSecurityKeyException;
import zenith_expense_tracker_nov_v1.repository.UserRepository;
import zenith_expense_tracker_nov_v1.utility.OTPGenerator;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class ForgotPasswordService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private OTPGenerator otpGenerator;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final Map<String, String> otpStore = new HashMap<>();
    private final Map<String, LocalDateTime> otpExpiry = new HashMap<>();

    private static final String PSC = "09876";

    public void sendOtp(String email) throws Exception {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new Exception("ERR_EMAIL_NOT_FOUND"));

        String otp = otpGenerator.generateOtp();
        otpStore.put(email, otp);
        otpExpiry.put(email, LocalDateTime.now().plusMinutes(5));

        emailService.sendOtp(email, otp);
    }

    public boolean verifyOtp(ForgotPasswordDTO dto) throws Exception {
        String storedOtp = otpStore.get(dto.getEmail());
        LocalDateTime expiry = otpExpiry.get(dto.getEmail());

        if (storedOtp == null || expiry == null || expiry.isBefore(LocalDateTime.now())) {
            throw new Exception("ERR_OTP_EXPIRED");
        }

        if (!storedOtp.equals(dto.getOtp())) {
            throw new Exception("ERR_OTP_MISMATCH");
        }

        otpStore.remove(dto.getEmail());
        otpExpiry.remove(dto.getEmail());
        return true;
    }

    public void verifySecurityKey(ForgotPasswordDTO dto) {
        if (!PSC.equals(dto.getSecurityKey())) {
            throw new InvalidSecurityKeyException("Invalid Security Key");
        }
    }

    public void resetPassword(ForgotPasswordDTO dto) throws Exception {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new Exception("ERR_EMAIL_NOT_FOUND"));



        if (passwordEncoder.matches(dto.getNewPassword(), user.getPassword())) {
            throw new Exception("ERR_OLD_PASSWORD");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);

        emailService.sendPasswordChangeConfirmation(dto.getEmail());
    }
}
