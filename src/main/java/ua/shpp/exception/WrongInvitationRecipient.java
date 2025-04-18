package ua.shpp.exception;

public class WrongInvitationRecipient extends RuntimeException {
    public WrongInvitationRecipient(String message) {
        super(message);
    }
}
