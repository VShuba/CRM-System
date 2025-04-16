package ua.shpp.exception;

public class BranchNotFoundException extends RuntimeException {
    public BranchNotFoundException(String message) {
        super(message);
    }

    public BranchNotFoundException() {
    }
}
