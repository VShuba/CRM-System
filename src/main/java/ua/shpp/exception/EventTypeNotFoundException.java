package ua.shpp.exception;

public class EventTypeNotFoundException extends RuntimeException {
    public EventTypeNotFoundException(String message) {
        super(message);
    }
}
