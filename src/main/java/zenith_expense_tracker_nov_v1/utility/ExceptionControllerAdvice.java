package zenith_expense_tracker_nov_v1.utility;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import zenith_expense_tracker_nov_v1.exception.UserAlreadyExistsException;
import zenith_expense_tracker_nov_v1.exception.UserNotFoundException;
import zenith_expense_tracker_nov_v1.exception.InvalidSecurityKeyException;

import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionControllerAdvice {
    @Autowired
    private Environment environment;

    // General exception handler
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorInfo> generalException(Exception exception) {
        ErrorInfo error = new ErrorInfo(
                exception.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorInfo> userNotFoundException(UserNotFoundException exception) {
        String msg = environment.getProperty("USER_NOT_FOUND", exception.getMessage());

        ErrorInfo error = new ErrorInfo(
                msg,
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    public ResponseEntity<ErrorInfo> validatorExceptionHandler(Exception exception) {
        String msg = "";

        if (exception instanceof MethodArgumentNotValidException methodArgumentNotValidException) {
            msg = methodArgumentNotValidException.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
        }

        else if (exception instanceof ConstraintViolationException constraintViolationException) {
            msg = constraintViolationException.getConstraintViolations().stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));
        }

        ErrorInfo error = new ErrorInfo(
                msg,
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorInfo> userAlreadyExistsException(UserAlreadyExistsException exception) {
        ErrorInfo error = new ErrorInfo(
                exception.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // Add handler for InvalidSecurityKeyException
    @ExceptionHandler(InvalidSecurityKeyException.class)
    public ResponseEntity<ErrorInfo> invalidSecurityKeyException(InvalidSecurityKeyException exception) {
        ErrorInfo error = new ErrorInfo(
                exception.getMessage(),
                HttpStatus.FORBIDDEN.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }
}
