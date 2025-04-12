package ua.shpp.exception;

public class OrganizationAlreadyExists extends RuntimeException {
    public OrganizationAlreadyExists(String message) {
        super(message);
    }
}

