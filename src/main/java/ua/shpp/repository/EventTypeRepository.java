package ua.shpp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.shpp.entity.EventTypeEntity;

import java.util.List;

@Repository
public interface EventTypeRepository extends JpaRepository<EventTypeEntity, Long> {
    boolean existsByNameAndBranchId(String name, Long branchId);
    List<EventTypeEntity> findAllByBranchId(Long branchId);
}
