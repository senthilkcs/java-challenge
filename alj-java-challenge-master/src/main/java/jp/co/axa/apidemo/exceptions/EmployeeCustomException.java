package jp.co.axa.apidemo.exceptions;

public class EmployeeCustomException extends RuntimeException {
    public EmployeeCustomException(String message, Throwable cause) {
        super(message, cause);
    }
}