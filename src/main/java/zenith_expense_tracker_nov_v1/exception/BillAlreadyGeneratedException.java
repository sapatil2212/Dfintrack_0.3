package zenith_expense_tracker_nov_v1.exception;

public class BillAlreadyGeneratedException extends RuntimeException {
    public BillAlreadyGeneratedException(String message) {
        super(message);
    }
}
