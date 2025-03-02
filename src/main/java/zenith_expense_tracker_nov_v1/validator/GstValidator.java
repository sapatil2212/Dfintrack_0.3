package zenith_expense_tracker_nov_v1.validator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.regex.Pattern;
@Component
@Slf4j
public class GstValidator {
    private static final String GST_PATTERN = "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z][0-9A-Z]{3}$";
    private static final Pattern pattern = Pattern.compile(GST_PATTERN);

    public boolean isValid(String gstNumber) {
        if (gstNumber == null || gstNumber.trim().isEmpty() || gstNumber.trim().equalsIgnoreCase("NA")) {
            return true;
        }
        boolean matches = pattern.matcher(gstNumber).matches();
        log.debug("GST number {} validation result: {}", gstNumber, matches);

        if (!matches) {
            log.error("GST number {} does not match the expected format", gstNumber);
            return false;
        }

        try {
            int stateCode = Integer.parseInt(gstNumber.substring(0, 2));
            if (stateCode < 1 || stateCode > 37) {
                log.error("Invalid state code: {}", stateCode);
                return false;
            }
        } catch (NumberFormatException e) {
            log.error("Error parsing state code", e);
            return false;
        }

        return true;
    }

    public String getGstValidationMessage() {
        return "GST number must follow the format: "
                + "\n- First 2 digits: State code (01-37)"
                + "\n- Next 5 characters: PAN number (uppercase letters)"
                + "\n- Next 4 digits: Entity number"
                + "\n- Next character: Alphabet (Z by default)"
                + "\n- Next 2 characters: Any alphanumeric"
                + "\n- Last character: Checksum (alphabet)";
    }
}