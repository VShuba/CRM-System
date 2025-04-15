package ua.shpp.exception;

public class OrganizationNotFound extends RuntimeException {
    public OrganizationNotFound(String message) {
        super(message);
    }

    public OrganizationNotFound() {
    }
}
