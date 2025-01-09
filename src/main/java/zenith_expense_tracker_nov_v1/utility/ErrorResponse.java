package zenith_expense_tracker_nov_v1.utility;



public class ErrorResponse {

    private String field;
    private String message;

    public ErrorResponse(String field, String message) {
        this.field = field;
        this.message = message;
    }

    // Getters and Setters
    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
