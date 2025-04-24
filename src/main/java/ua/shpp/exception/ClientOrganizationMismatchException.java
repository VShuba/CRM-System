package ua.shpp.exception;

public class ClientOrganizationMismatchException extends RuntimeException {
    public ClientOrganizationMismatchException() {
        super("Client does not belong to the specified organization");
    }
}
