package ua.shpp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.shpp.entity.EventTypeEntity;

@Repository
public interface EventTypeRepository extends JpaRepository<EventTypeEntity, Long> {
    boolean existsByName(String name);
}
