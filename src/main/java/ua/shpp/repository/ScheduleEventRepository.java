package ua.shpp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.shpp.entity.ScheduleEventEntity;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleEventRepository extends JpaRepository<ScheduleEventEntity, Long> {
    List<ScheduleEventEntity> findByOrganizationIdAndEventDateBetween(Long id,LocalDate startDate, LocalDate endDate);

    @Query(value = """
            SELECT se.*
              FROM schedule_event se
               WHERE (organization_id = :orgId)
               AND (:roomId      IS NULL OR se.room_id       = :roomId)
               AND (:employeeId  IS NULL OR se.employee_id   = :employeeId)
               AND (:eventTypeId IS NULL OR se.event_type_id = :eventTypeId)
               AND (:serviceId   IS NULL OR se.service_id    = :serviceId)
               AND (:startDate   IS NULL OR se.event_date    >= :startDate)
               AND (:endDate     IS NULL OR se.event_date    <= :endDate)
            """,
            nativeQuery = true)
    List<ScheduleEventEntity> findFilteredByAll(
            @Param("orgId") Long orgId,
            @Param("roomId") Long roomId,
            @Param("employeeId") Long employeeId,
            @Param("eventTypeId") Long eventTypeId,
            @Param("serviceId") Long serviceId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT se.serviceEntity.id FROM ScheduleEventEntity se WHERE se.id = :eventId")
    Long findServiceIdByEventId(@Param("eventId") Long eventId);
}
