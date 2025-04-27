package ua.shpp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.shpp.entity.payment.OneTimeInfoEntity;
import java.util.Optional;

public interface OneTimeInfoRepository extends JpaRepository<OneTimeInfoEntity, Long> {

    Optional<OneTimeInfoEntity> getByPaymentCheckId(Long id);
}
