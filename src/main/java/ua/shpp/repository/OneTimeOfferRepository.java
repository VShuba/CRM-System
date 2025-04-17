package ua.shpp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.shpp.entity.OneTimeServiceEntity;

import java.util.Optional;


public interface OneTimeOfferRepository extends JpaRepository<OneTimeServiceEntity, Long> {
    @Override
    Optional<OneTimeServiceEntity> findById(Long aLong);

    boolean existsById(Long id);
}
