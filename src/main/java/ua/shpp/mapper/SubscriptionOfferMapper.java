package ua.shpp.mapper;

import org.mapstruct.*;
import ua.shpp.dto.SubscriptionOfferDTO;
import ua.shpp.entity.ServiceEntity;
import ua.shpp.entity.SubscriptionServiceEntity;
import ua.shpp.exception.ServiceNotFoundException;
import ua.shpp.repository.ServiceRepository;

import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface SubscriptionOfferMapper {
    @Mapping(target = "termOfValidityInDays", qualifiedByName = "periodToInt")
    @Mapping(target = "activity",
            source = "activity",
            qualifiedByName = "activityToId")
    SubscriptionOfferDTO toDto(SubscriptionServiceEntity entity);

    @Mapping(target = "termOfValidityInDays", qualifiedByName = "intToPeriod")
    @Mapping(target = "activity", source = "activity",
            qualifiedByName = "idToActivity")
    SubscriptionServiceEntity toEntity(SubscriptionOfferDTO dto,
                                       @Context ServiceRepository repository);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "activity", source = "activity",
            qualifiedByName = "idToActivity")
    void updateFromDto(SubscriptionOfferDTO dto,
                       @MappingTarget SubscriptionServiceEntity entity,
                       @Context ServiceRepository repository);

    @Named("periodToInt")
    static long durationToLong(Period period) {
        return period != null ? period.getDays() : 0;
    }

    @Named("intToPeriod")
    static Period longToDuration(int days) {
        return Period.ofDays(days);
    }

    @Named("activityToId")
    static List<Long> activityToId(List<ServiceEntity> entityList) {
        List<Long> idList = new ArrayList<>();
        if (entityList != null) {
            for (ServiceEntity entity : entityList) {
                idList.add(entity.getId());
            }
        }
        return idList;
    }

    @Named("idToActivity")
    default List<ServiceEntity> idToActivity(List<Long> idList,
                                       @Context ServiceRepository repository) {
        List<ServiceEntity> entityList = new ArrayList<>();
        if (idList != null){
            for (Long id : idList){
               var entity = repository.findById(id)
                        .orElseThrow(() -> new ServiceNotFoundException("Service not found: " + id));
                entityList.add(entity);
            }
        }
        return entityList;
    }
}
