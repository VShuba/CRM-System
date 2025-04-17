package ua.shpp.exception;

public class EventTypeAlreadyExistsException extends RuntimeException {
    public EventTypeAlreadyExistsException(String message) {
        super(message);
    }
}
