package ua.shpp.exception;

public class MissingSubscriptionServiceException  extends RuntimeException{
    public MissingSubscriptionServiceException(Long subscriptionId) {
        super("SubscriptionService is null for ID: " + subscriptionId);
    }
}
