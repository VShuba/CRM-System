package ua.shpp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ua.shpp.dto.OneTimeOfferDTO;
import ua.shpp.entity.OneTimeServiceEntity;

import java.time.Duration;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface OneTimeOfferMapper {
    @Mapping(target = "durationInMinutes", qualifiedByName = "durationToLong")
    OneTimeOfferDTO entityToDto(OneTimeServiceEntity entity);
    @Mapping(target = "durationInMinutes", qualifiedByName = "longToDuration")
    OneTimeServiceEntity dtoToEntity(OneTimeOfferDTO dto);

    @Named("durationToLong")
    static long durationToLong(Duration duration) {
        return duration != null ? duration.toMinutes() : 0;
    }

    @Named("longToDuration")
    static Duration longToDuration(long minutes) {
        return Duration.ofMinutes(minutes);
    }
}
