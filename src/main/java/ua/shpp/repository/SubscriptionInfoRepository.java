package ua.shpp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.shpp.entity.payment.SubscriptionInfoEntity;
import java.util.Optional;

public interface SubscriptionInfoRepository extends JpaRepository<SubscriptionInfoEntity, Long> {
    Optional<SubscriptionInfoEntity> getByPaymentCheckId(Long id);
}
