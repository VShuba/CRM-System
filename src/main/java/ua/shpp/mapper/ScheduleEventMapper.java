package ua.shpp.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ua.shpp.dto.ScheduleEventCreateDto;
import ua.shpp.dto.ScheduleEventDto;
import ua.shpp.entity.*;
import ua.shpp.exception.EventNotFoundException;
import ua.shpp.exception.RoomNotFoundException;
import ua.shpp.exception.ServiceNotFoundException;
import ua.shpp.repository.EmployeeRepository;
import ua.shpp.repository.EventTypeRepository;
import ua.shpp.repository.RoomRepository;
import ua.shpp.repository.ServiceRepository;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface ScheduleEventMapper {
    @Mapping(target = "serviceEntity",
            source = "serviceId",
            qualifiedByName = "idToService")
    @Mapping(target = "employee", source = "trainerId", qualifiedByName = "idToTrainer")
    @Mapping(target = "room", source = "roomId", qualifiedByName = "idToRoom")
    @Mapping(target = "eventType", source = "eventTypeId", qualifiedByName = "idToEventType")
    @Mapping(target = "organization", source = "serviceId", qualifiedByName = "setOrganization")
    ScheduleEventEntity toEntity(ScheduleEventDto dto,
                                 @Context ServiceRepository serviceRepository,
                                 @Context EmployeeRepository employeeRepository,
                                 @Context RoomRepository roomRepository,
                                 @Context EventTypeRepository eventTypeRepository
    );

    @Mapping(target = "serviceEntity",
            source = "serviceId",
            qualifiedByName = "idToService")
    @Mapping(target = "employee", source = "trainerId", qualifiedByName = "idToTrainer")
    @Mapping(target = "room", source = "roomId", qualifiedByName = "idToRoom")
    @Mapping(target = "eventType", source = "eventTypeId", qualifiedByName = "idToEventType")
    @Mapping(target = "organization", source = "serviceId", qualifiedByName = "setOrganization")
    ScheduleEventEntity toEntity(ScheduleEventCreateDto dto,
                                 @Context ServiceRepository serviceRepository,
                                 @Context EmployeeRepository employeeRepository,
                                 @Context RoomRepository roomRepository,
                                 @Context EventTypeRepository eventTypeRepository
    );

    @Mapping(target = "serviceId",
            source = "serviceEntity",
            qualifiedByName = "serviceToId")
    @Mapping(target = "trainerId", source = "employee", qualifiedByName = "trainerToId")
    @Mapping(target = "roomId", source = "room", qualifiedByName = "roomToId")
    @Mapping(target = "eventTypeId", source = "eventType", qualifiedByName = "eventTypeToId")
    ScheduleEventDto toDto(ScheduleEventEntity entity);

    @Named("serviceToId")
    static Long serviceToId(ServiceEntity entity) {
        return entity != null ? entity.getId() : null;
    }

    @Named("idToService")
    default ServiceEntity idToService(Long id,
                                      @Context ServiceRepository repository) {
        return repository.findById(id)
                .orElseThrow(() -> new ServiceNotFoundException("Service not found: " + id));
    }

    @Named("trainerToId")
    static Long trainerToId(EmployeeEntity entity) {
        return entity != null ? entity.getId() : null;
    }

    @Named("idToTrainer")
    default EmployeeEntity idToTrainer(Long id, @Context EmployeeRepository repository) {
        if (id == null) {
            return null;
        }
        return repository.findById(id).orElseThrow(() -> null);
    }

    @Named("roomToId")
    static Long roomToId(RoomEntity entity) {
        return entity != null ? entity.getId() : null;
    }

    @Named("idToRoom")
    default RoomEntity idToRoom(Long id, @Context RoomRepository repository) {
        if (id == null) {
            return null;
        }
        return repository.findById(id).orElseThrow(() -> new RoomNotFoundException("Room not found: " + id));
    }

    @Named("idToEventType")
    default EventTypeEntity idToEventType(Long id, @Context EventTypeRepository repository) {
        return repository.findById(id).orElseThrow(() -> new EventNotFoundException("Event not found: " + id));
    }

    @Named("eventTypeToId")
    default Long eventTypeToId(EventTypeEntity entity) {
        if (entity == null) {
            return null;
        }
        return entity.getId();
    }

    @Named("setOrganization")
    default Organization setOrganization(Long id,
                                      @Context ServiceRepository repository) {
        return repository.findById(id)
                .orElseThrow(() -> new ServiceNotFoundException("Service not found: " + id))
                .getBranch()
                .getOrganization();
    }
}
