package ua.shpp.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ua.shpp.dto.ScheduleEventDto;
import ua.shpp.entity.ScheduleEventEntity;
import ua.shpp.entity.ServiceEntity;
import ua.shpp.exception.ServiceNotFoundException;
import ua.shpp.repository.ServiceRepository;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface ScheduleEventMapper {
    @Mapping(target = "serviceEntity",
            source = "serviceId",
            qualifiedByName = "idToService")
    ScheduleEventEntity toEntity(ScheduleEventDto dto,
                                 @Context ServiceRepository serviceRepository);

    @Mapping(target = "serviceId",
            source = "serviceEntity",
            qualifiedByName = "serviceToId")
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
}
