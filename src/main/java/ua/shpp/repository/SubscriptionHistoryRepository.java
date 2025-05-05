package ua.shpp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.shpp.entity.SubscriptionHistoryEntity;

import java.util.List;
import java.util.Optional;

public interface SubscriptionHistoryRepository extends JpaRepository<SubscriptionHistoryEntity, Long> {
    List<SubscriptionHistoryEntity> findByClientIdAndIsValid(Long clientId, Boolean isValid);

    List<SubscriptionHistoryEntity> findByClientId(Long clientId);

    Optional<SubscriptionHistoryEntity> findByClientIdAndNameAndEventTypeAndIsValid(Long clientId, String name,
                                                                                    String eventType, Boolean isValid);
}
