package ua.shpp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.shpp.entity.EventTypeEntity;

import java.util.List;

@Repository
public interface EventTypeRepository extends JpaRepository<EventTypeEntity, Long>,
        JpaSpecificationExecutor<EventTypeEntity> {
    boolean existsByNameAndBranchId(String name, Long branchId);
    List<EventTypeEntity> findAllByBranchId(Long branchId);
    @Query("SELECT et.branch.id FROM EventTypeEntity et WHERE et.id = :eventTypeId")
    Long findBranchIdByEventTypeId(@Param("eventTypeId") Long eventTypeId);
}
