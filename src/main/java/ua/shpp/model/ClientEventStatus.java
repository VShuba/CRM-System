package ua.shpp.model;

public enum ClientEventStatus {
    ASSIGNED(0),
    SKIPPED(1),
    USED(1);

    private final int order;

    ClientEventStatus(int i) {
        this.order = i;
    }

    public int getOrder() {
        return order;
    }

    public static boolean checkIfStatusChangePossible(ClientEventStatus oldStatus, ClientEventStatus newStatus) {
        if (oldStatus == SKIPPED || oldStatus == USED) {
            return false;
        }
        return oldStatus.order < newStatus.order;

    }

}
