package ua.shpp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.shpp.entity.payment.OneTimeInfoEntity;

public interface OneTimeInfoRepository extends JpaRepository<OneTimeInfoEntity, Long> {
}
