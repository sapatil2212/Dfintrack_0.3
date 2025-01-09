package zenith_expense_tracker_nov_v1.exception;

public class UserNotFoundException extends RuntimeException {


    public UserNotFoundException(String message) {
        super(message);
    }


    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}