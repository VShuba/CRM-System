package ua.shpp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.shpp.entity.payment.OneTimeDealEntity;
import java.util.Optional;

public interface OneTimeDealRepository extends JpaRepository<OneTimeDealEntity, Long> {

    Optional<OneTimeDealEntity> getByPaymentCheckId(Long id);
}
