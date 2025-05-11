package ua.shpp.exception;

public class GoogleSheetsNotAuthorizedException extends RuntimeException {
    public GoogleSheetsNotAuthorizedException(String message) {
        super(message);
    }
}
