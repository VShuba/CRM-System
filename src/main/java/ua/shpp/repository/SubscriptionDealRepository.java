package ua.shpp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.shpp.entity.payment.SubscriptionDealEntity;

import java.util.Optional;

public interface SubscriptionDealRepository extends JpaRepository<SubscriptionDealEntity, Long> {
    Optional<SubscriptionDealEntity> getByPaymentCheckId(Long id);

    Optional<SubscriptionDealEntity> findByClientIdAndSubscriptionService_NameAndSubscriptionService_EventType_Name(
            Long clientId, String name, String eventType);

    @Query("SELECT sd.client.id FROM SubscriptionDealEntity sd WHERE sd.id = :dealId")
    Long findClientIdBySubscriptionDealId(@Param("dealId") Long dealId);
}
