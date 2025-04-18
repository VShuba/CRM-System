package ua.shpp.exception;

public class InvitationExpiredException extends RuntimeException {
    public InvitationExpiredException(String message) {
        super(message);
    }
}
