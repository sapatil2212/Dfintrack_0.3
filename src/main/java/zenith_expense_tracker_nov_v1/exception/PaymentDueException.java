package zenith_expense_tracker_nov_v1.exception;



public class PaymentDueException extends RuntimeException {
    public PaymentDueException(String message) {
        super(message);
    }
}