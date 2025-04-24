package ua.shpp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.shpp.entity.ScheduleEventEntity;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleEventRepository extends JpaRepository<ScheduleEventEntity, Long> {
    List<ScheduleEventEntity> findByEventDateBetween(LocalDate startDate, LocalDate endDate);
}
