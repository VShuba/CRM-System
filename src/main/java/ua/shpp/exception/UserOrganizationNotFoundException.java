package ua.shpp.exception;

public class UserOrganizationNotFoundException extends RuntimeException {
    public UserOrganizationNotFoundException(String message) {
        super(message);
    }
}
