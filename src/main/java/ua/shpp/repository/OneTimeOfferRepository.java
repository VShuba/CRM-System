package ua.shpp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ua.shpp.entity.OneTimeOfferEntity;


public interface OneTimeOfferRepository extends JpaRepository<OneTimeOfferEntity, Long> {
    Page<OneTimeOfferEntity> findAllByEventTypeId(Long eventTypeId, Pageable pageRequest);
}
