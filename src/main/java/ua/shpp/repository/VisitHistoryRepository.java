package ua.shpp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.shpp.entity.VisitHistoryEntity;

import java.util.List;

@Repository
public interface VisitHistoryRepository extends JpaRepository<VisitHistoryEntity, Long> {
    List<VisitHistoryEntity> findAllByClientId(Long clientId);
}
