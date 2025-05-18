package ua.shpp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ua.shpp.dto.ScheduleEventCreateDto;
import ua.shpp.dto.ScheduleEventDto;
import ua.shpp.entity.ScheduleEventEntity;
import ua.shpp.exception.EventNotFoundException;
import ua.shpp.mapper.ScheduleEventMapper;
import ua.shpp.repository.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleEventService {
    private final ScheduleEventRepository scheduleEventRepository;
    private final ScheduleEventMapper scheduleEventMapper;
    private final ServiceRepository serviceRepository;
    private final EmployeeRepository employeeRepository;
    private final RoomRepository roomRepository;
    private final EventTypeRepository eventTypeRepository;

    public ScheduleEventDto create(ScheduleEventCreateDto scheduleEventDto) {
        log.debug("create() called with DTO: {}", scheduleEventDto);

        var entity = scheduleEventMapper.toEntity(scheduleEventDto,
                serviceRepository,
                employeeRepository,
                roomRepository,
                eventTypeRepository);
        entity = scheduleEventRepository.save(entity);
        log.info("Created schedule event (id={})", entity.getId());
        log.debug("create() schedule event Entity: {}", entity);
        return scheduleEventMapper.toDto(entity);
    }

    public ScheduleEventDto getById(Long id) {
        log.debug("get() called with id: {}", id);
        var entity = scheduleEventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException(String.format("Event id: %d, not found", id)));
        log.info("Fetching schedule event (id={})", id);
        log.debug("Fetching schedule event entity: {}", entity);
        return scheduleEventMapper.toDto(entity);
    }

    public List<ScheduleEventDto> getAllBetweenDates(Long orgId, LocalDate startDate, LocalDate endDate) {
        log.debug("getAllBetweenDates() called with from {} to {} dates", startDate, endDate);
        var listEntity = scheduleEventRepository
                .findByOrganizationIdAndEventDateBetween(orgId, startDate, endDate);
        log.debug("dates between {} to {},\n events:\n {} ",
                startDate, endDate, listEntity);
        log.info("dates between {} to {} have {} events",
                startDate, endDate, listEntity.size());
        return listEntity.stream().map(scheduleEventMapper::toDto).toList();
    }

    public List<ScheduleEventDto> eventFilter(
                                              Long orgId,
                                              Long roomId,
                                              Long employeeId,
                                              Long serviceId,
                                              Long eventTypeId,
                                              LocalDate startDate,
                                              LocalDate endDate) {
        log.debug("eventFilter() called with room id: {}, employee id: {}, service id:{}, event type id: {}, from {} to {} dates",
                roomId, employeeId, serviceId, eventTypeId, startDate, endDate);

        List<ScheduleEventEntity> list =  scheduleEventRepository.findFilteredByAll(
                orgId, roomId, employeeId, serviceId, eventTypeId, startDate, endDate);
        log.info("eventFilter() found {} events",list.size());
        log.debug("Found events: {}",list);
        return list.stream().map(scheduleEventMapper::toDto).toList();
    }

    public void deleteById(Long id) {
        log.debug("delete() called with id: {}", id);
        if (!scheduleEventRepository.existsById(id)) {
            log.debug("Event id: {}, not found", id);
        } else {
            scheduleEventRepository.deleteById(id);
            log.info("Deleted event (id={})", id);
        }
    }
}
