package ua.shpp.exception;

public class EventForClientNotFoundException extends RuntimeException {
    public EventForClientNotFoundException(Long clientId, Long scheduleEventId) {
        super("Not found schedule event with id " + scheduleEventId + " for client with id " + clientId);
    }
}
