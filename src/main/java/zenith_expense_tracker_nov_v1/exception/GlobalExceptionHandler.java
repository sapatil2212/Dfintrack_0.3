package zenith_expense_tracker_nov_v1.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import zenith_expense_tracker_nov_v1.utility.ErrorResponse;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(OldPasswordException.class)
    public ResponseEntity<ErrorResponse> handleOldPasswordException(OldPasswordException ex) {
        ErrorResponse errorResponse = new ErrorResponse("oldPassword", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


}
