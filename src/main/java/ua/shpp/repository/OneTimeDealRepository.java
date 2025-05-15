package ua.shpp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.shpp.entity.payment.OneTimeDealEntity;
import java.util.Optional;

public interface OneTimeDealRepository extends JpaRepository<OneTimeDealEntity, Long> {

    Optional<OneTimeDealEntity> getByPaymentCheckId(Long id);

    @Query("SELECT o.client.id FROM OneTimeDealEntity o WHERE o.id = :dealId")
    Long findClientIdByOneTimeDealId(@Param("dealId") Long dealId);
}
