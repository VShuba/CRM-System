package ua.shpp.exception;

public class VisitAlreadyUsedException extends RuntimeException {
    public VisitAlreadyUsedException(String message) {
        super(message);
    }
}
