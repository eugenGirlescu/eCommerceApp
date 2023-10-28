package com.example.ecommerceapp.advice;

import com.example.ecommerceapp.exception.EmailFailureException;
import com.example.ecommerceapp.exception.UserAlreadyExistsException;
import com.example.ecommerceapp.exception.UserNameOrPasswordNotFoundException;
import com.example.ecommerceapp.exception.UserNotVerifiedException;
import jakarta.validation.constraints.Email;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Object> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", new Date());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Bad Request");
        response.put("message", "Validation failed");
        response.put("errors", errors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotVerifiedException.class)
    public ResponseEntity<String> handleUserNotVerifiedException(UserNotVerifiedException ex) {
        if (ex.isNewEmailSent()) {
            return new ResponseEntity<>("User not verified. A new verification email has been sent.", HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>("User not verified.", HttpStatus.FORBIDDEN);
        }
    }

    @ExceptionHandler(EmailFailureException.class)
    public ResponseEntity<String> handleEmailFailureException(EmailFailureException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserNameOrPasswordNotFoundException.class)
    public ResponseEntity<String> handleUserNameOrPasswordNotFoundException(UserNameOrPasswordNotFoundException ex) {
        return new ResponseEntity<>("User or password are not ok.", HttpStatus.NOT_FOUND);
    }
}
