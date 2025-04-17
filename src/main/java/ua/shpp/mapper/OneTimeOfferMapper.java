package ua.shpp.mapper;

import org.mapstruct.*;
import ua.shpp.dto.OneTimeOfferDTO;
import ua.shpp.entity.OneTimeServiceEntity;
import ua.shpp.entity.ServiceEntity;
import ua.shpp.exception.ServiceNotFoundException;
import ua.shpp.repository.ServiceRepository;

import java.time.Duration;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface OneTimeOfferMapper {

    @Mapping(target = "durationInMinutes", source = "durationInMinutes",
            qualifiedByName = "durationToLong")
    @Mapping(target = "activity",
            source = "activity",
            qualifiedByName = "activityToId")
    OneTimeOfferDTO entityToDto(OneTimeServiceEntity entity);

    @Mapping(target = "durationInMinutes", source = "durationInMinutes",
            qualifiedByName = "longToDuration")
    @Mapping(target = "activity", source = "activity",
            qualifiedByName = "idToActivity")
    OneTimeServiceEntity dtoToEntity(OneTimeOfferDTO dto,
                                     @Context ServiceRepository repository);

    @BeanMapping(nullValuePropertyMappingStrategy =
            NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "activity", source = "activity",
            qualifiedByName = "idToActivity")
    void updateFromDto(OneTimeOfferDTO dto,
                       @MappingTarget OneTimeServiceEntity entity,
                       @Context ServiceRepository repository);

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
}