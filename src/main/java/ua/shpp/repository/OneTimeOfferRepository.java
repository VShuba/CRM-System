package ua.shpp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ua.shpp.entity.OneTimeServiceEntity;


public interface OneTimeOfferRepository extends JpaRepository<OneTimeServiceEntity, Long> {
    Page<OneTimeServiceEntity> findAllByEventTypeId(Long eventTypeId, Pageable pageRequest);
}
