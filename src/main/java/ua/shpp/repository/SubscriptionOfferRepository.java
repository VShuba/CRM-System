package ua.shpp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.shpp.entity.SubscriptionServiceEntity;


public interface SubscriptionOfferRepository extends JpaRepository<SubscriptionServiceEntity, Long> {
}
