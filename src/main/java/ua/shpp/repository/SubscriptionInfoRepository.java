package ua.shpp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.shpp.entity.payment.SubscriptionInfoEntity;

public interface SubscriptionInfoRepository extends JpaRepository<SubscriptionInfoEntity, Long> {
}
