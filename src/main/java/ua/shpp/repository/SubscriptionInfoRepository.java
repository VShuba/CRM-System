package ua.shpp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.shpp.entity.payment.SubscriptionInfoEntity;
import java.util.Optional;

public interface SubscriptionInfoRepository extends JpaRepository<SubscriptionInfoEntity, Long> {
    Optional<SubscriptionInfoEntity> getByPaymentCheckId(Long id);

    Optional<SubscriptionInfoEntity> findByClientIdAndSubscriptionService_NameAndSubscriptionService_EventType_Name(
            Long clientId, String name, String eventType);
}
