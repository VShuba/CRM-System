package ua.shpp.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ua.shpp.dto.ScheduleEventDto;
import ua.shpp.entity.EmployeeEntity;
import ua.shpp.entity.RoomEntity;
import ua.shpp.entity.ScheduleEventEntity;
import ua.shpp.entity.ServiceEntity;
import ua.shpp.exception.RoomNotFoundException;
import ua.shpp.exception.ServiceNotFoundException;
import ua.shpp.repository.EmployeeRepository;
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
    ScheduleEventEntity toEntity(ScheduleEventDto dto,
                                 @Context ServiceRepository serviceRepository,
                                 @Context EmployeeRepository employeeRepository,
                                 @Context RoomRepository roomRepository
    );

    @Mapping(target = "serviceId",
            source = "serviceEntity",
            qualifiedByName = "serviceToId")
    @Mapping(target = "trainerId", source = "employee", qualifiedByName = "trainerToId")
    @Mapping(target = "roomId", source = "room", qualifiedByName = "roomToId")
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
}
