package ua.shpp.exception;

public class BranchOrganizationMismatchException extends RuntimeException {
    public BranchOrganizationMismatchException(Long branchId, Long orgId) {
        super("Branch with ID " + branchId + " does not belong to organization with ID " + orgId);
    }
}
