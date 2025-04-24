package ua.shpp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.shpp.dto.ScheduleEventDto;
import ua.shpp.exception.EventTypeNotFoundException;
import ua.shpp.exception.OfferNotFoundException;
import ua.shpp.mapper.ScheduleEventMapper;
import ua.shpp.repository.ScheduleEventRepository;
import ua.shpp.repository.ServiceRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleEventService {
    private final ScheduleEventRepository scheduleEventRepository;
    private final ScheduleEventMapper scheduleEventMapper;
    private final ServiceRepository serviceRepository;

    public ScheduleEventDto create(ScheduleEventDto scheduleEventDto) {
        var entity = scheduleEventMapper.toEntity(scheduleEventDto,serviceRepository);
        entity.setId(null);
        entity = scheduleEventRepository.save(entity);

        return scheduleEventMapper.toDto(entity);
    }

    public ScheduleEventDto getById (Long id){
        var entity = scheduleEventRepository.findById(id).orElseThrow(() -> new RuntimeException(String.format("Event id: %d, not found", id)));
        return scheduleEventMapper.toDto(entity);
    }

    public List<ScheduleEventDto> getAllBetweenDates(LocalDate startDate, LocalDate endDate){
        var listEntity = scheduleEventRepository.findByEventDateBetween(startDate, endDate);
        return listEntity.stream().map(scheduleEventMapper::toDto).toList();
    }
    public void deleteById(Long id){
        scheduleEventRepository.deleteById(id);
    }
}
