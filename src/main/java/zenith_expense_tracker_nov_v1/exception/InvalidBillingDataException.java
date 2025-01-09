package zenith_expense_tracker_nov_v1.exception;

public class InvalidBillingDataException extends RuntimeException {
    public InvalidBillingDataException(String message) {
        super(message);
    }
}