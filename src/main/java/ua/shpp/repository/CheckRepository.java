package ua.shpp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.shpp.entity.payment.CheckEntity;

public interface CheckRepository extends JpaRepository<CheckEntity, Long> {
}
