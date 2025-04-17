package ua.shpp.mapper;

import org.mapstruct.*;
import ua.shpp.dto.SubscriptionOfferDTO;
import ua.shpp.entity.SubscriptionServiceEntity;

import java.time.Period;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface SubscriptionOfferMapper {
    @Mapping(target = "termOfValidityInDays", qualifiedByName = "periodToInt")
    SubscriptionOfferDTO toDto(SubscriptionServiceEntity entity);

    @Mapping(target = "termOfValidityInDays", qualifiedByName = "intToPeriod")
    SubscriptionServiceEntity toEntity(SubscriptionOfferDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(SubscriptionOfferDTO dto,
                       @MappingTarget SubscriptionServiceEntity entity);

    @Named("periodToInt")
    static long durationToLong(Period period) {
        return period != null ? period.getDays() : 0;
    }

    @Named("intToPeriod")
    static Period longToDuration(int days) {
        return Period.ofDays(days);
    }
}
