package ua.shpp.mapper;

import org.mapstruct.*;
import ua.shpp.dto.OneTimeOfferDTO;
import ua.shpp.entity.EventTypeEntity;
import ua.shpp.entity.OneTimeServiceEntity;
import ua.shpp.entity.ServiceEntity;
import ua.shpp.exception.EventTypeNotFoundException;
import ua.shpp.exception.ServiceNotFoundException;
import ua.shpp.repository.EventTypeRepository;
import ua.shpp.repository.ServiceRepository;

import java.time.Duration;
import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface OneTimeOfferMapper {

    @Mapping(target = "durationInMinutes", source = "durationInMinutes",
            qualifiedByName = "durationToLong")
    @Mapping(target = "activityId",
            source = "activity",
            qualifiedByName = "activityToId")
    @Mapping(target = "eventTypeId", source = "eventType",
            qualifiedByName = "eventToId")
    OneTimeOfferDTO entityToDto(OneTimeServiceEntity entity);

    @Mapping(target = "durationInMinutes", source = "durationInMinutes",
            qualifiedByName = "longToDuration")
    @Mapping(target = "activity", source = "activityId",
            qualifiedByName = "idToActivity")
    @Mapping(target = "eventType", source = "eventTypeId",
            qualifiedByName = "idToEvent")
    OneTimeServiceEntity dtoToEntity(OneTimeOfferDTO dto,
                                     @Context ServiceRepository serviceRepository,
                                     @Context EventTypeRepository eventTypeRepository);

    @BeanMapping(nullValuePropertyMappingStrategy =
            NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "activity", source = "activityId",
            qualifiedByName = "idToActivity")
    @Mapping(target = "eventType", source = "eventTypeId",
            qualifiedByName = "idToEvent")
    void updateFromDto(OneTimeOfferDTO dto,
                       @MappingTarget OneTimeServiceEntity entity,
                       @Context ServiceRepository serviceRepository,
                       @Context EventTypeRepository eventTypeRepository);

    @Named("durationToLong")
    static long durationToLong(Duration duration) {
        return duration != null ? duration.toMinutes() : 0L;
    }

    @Named("longToDuration")
    static Duration longToDuration(long minutes) {
        return Duration.ofMinutes(minutes);
    }

    @Named("activityToId")
    static Long activityToId(ServiceEntity entity) {
        return entity != null ? entity.getId() : null;
    }

    @Named("idToActivity")
    default ServiceEntity idToActivity(Long id,
                                       @Context ServiceRepository repository) {
        return repository.findById(id)
                .orElseThrow(() -> new ServiceNotFoundException("Service not found: " + id));
    }

//    @Named("eventToId")
//    static Long eventToId(EventTypeEntity entity) {
//        return entity != null ? entity.getId() : null;
//    }
@Named("eventToId")
static List<Long> eventToId(List<EventTypeEntity> entity) {
    return entity != null ? entity.stream().map(EventTypeEntity::getId).toList() : null;
}

    @Named("idToEvent")
    default EventTypeEntity idToEvent(Long id,
                                             @Context EventTypeRepository repository) {
        return repository.findById(id)
                .orElseThrow(() -> new EventTypeNotFoundException("Event type not found: " + id));
    }
}