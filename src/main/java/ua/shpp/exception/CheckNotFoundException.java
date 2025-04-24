package ua.shpp.exception;

public class CheckNotFoundException extends RuntimeException {
    public CheckNotFoundException(String message) {
        super(message);
    }
}