package ua.shpp.exception;

public class ClientEventStatusChnageException extends RuntimeException {
    public ClientEventStatusChnageException() {
        super("Cannot change status");
    }

    public ClientEventStatusChnageException(String message) {
        super(message);
    }
}
