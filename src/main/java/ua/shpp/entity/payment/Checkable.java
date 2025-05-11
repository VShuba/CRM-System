package ua.shpp.entity.payment;

public sealed interface Checkable permits OneTimeDealEntity, SubscriptionDealEntity {
    //This is a marker interface for classes that store a check.
}
