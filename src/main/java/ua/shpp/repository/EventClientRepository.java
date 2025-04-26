package ua.shpp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.shpp.entity.EventClientEntity;
import ua.shpp.entity.EventClientId;

public interface EventClientRepository extends JpaRepository<EventClientEntity, EventClientId> {
}
