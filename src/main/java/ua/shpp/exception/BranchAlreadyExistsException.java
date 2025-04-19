package ua.shpp.exception;

public class BranchAlreadyExistsException extends RuntimeException {
    public BranchAlreadyExistsException(String s) {
        super(s);
    }
}
