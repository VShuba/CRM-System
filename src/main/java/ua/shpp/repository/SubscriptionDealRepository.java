package ua.shpp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.shpp.entity.payment.SubscriptionDealEntity;

import java.util.Optional;

public interface SubscriptionDealRepository extends JpaRepository<SubscriptionDealEntity, Long> {
    Optional<SubscriptionDealEntity> getByPaymentCheckId(Long id);

    Optional<SubscriptionDealEntity> findByClientIdAndSubscriptionService_NameAndSubscriptionService_EventType_Name(
            Long clientId, String name, String eventType);
}
