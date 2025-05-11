package ua.shpp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ua.shpp.entity.SubscriptionOfferEntity;


public interface SubscriptionOfferRepository extends JpaRepository<SubscriptionOfferEntity, Long> {
    Page<SubscriptionOfferEntity> findAllByEventTypeId(Long eventTypeId, Pageable pageRequest);
}
