package jp.co.axa.apidemo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the RESTful API.
 * Handles exceptions thrown by the application and maps them to HTTP response entities.
 */
@ControllerAdvice
public class ApplicationExceptionHandler  {

    /**
     * Handles IllegalArgumentException and returns a BAD_REQUEST response.
     */
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    protected ResponseEntity<Object> handleBadRequest(MethodArgumentNotValidException ex) {
        Map<String, String> errorMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errorMap.put(error.getField(), error.getDefaultMessage())
        );

        ApiError apiError = new ApiError(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST,
                "Validation error: Invalid argument",
                errorMap.toString());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    /**
     * Handles ResourceNotFoundException and returns a NOT_FOUND response.
     */
    @ExceptionHandler(value = {ResourceNotFoundException.class})
    protected ResponseEntity<Object> handleNotFound(RuntimeException ex) {
        ApiError apiError = new ApiError(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND,
                "Resource Not Found",
                ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    /**
     * Handles all other exceptions and returns an INTERNAL_SERVER_ERROR response.
     */
    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleServerError(RuntimeException ex) {
        ApiError apiError = new ApiError(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal server error",
                ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
