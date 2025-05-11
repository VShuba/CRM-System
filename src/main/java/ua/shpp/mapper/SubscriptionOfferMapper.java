package ua.shpp.mapper;

import org.mapstruct.*;
import ua.shpp.dto.SubscriptionOfferCreateDTO;
import ua.shpp.dto.SubscriptionOfferDTO;
import ua.shpp.entity.EventTypeEntity;
import ua.shpp.entity.ServiceEntity;
import ua.shpp.entity.SubscriptionOfferEntity;
import ua.shpp.exception.EventTypeNotFoundException;
import ua.shpp.exception.ServiceNotFoundException;
import ua.shpp.repository.EventTypeRepository;
import ua.shpp.repository.ServiceRepository;

import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface SubscriptionOfferMapper {
    @Mapping(target = "termOfValidityInDays", qualifiedByName = "periodToInt")
    @Mapping(target = "activitiesId",
            source = "activities",
            qualifiedByName = "activityToId")
    @Mapping(target = "eventTypeId",
            source = "eventType",
            qualifiedByName = "eventToId")
    SubscriptionOfferDTO toDto(SubscriptionOfferEntity entity);

    @Mapping(target = "termOfValidityInDays", qualifiedByName = "intToPeriod")
    @Mapping(target = "activities", source = "activitiesId",
            qualifiedByName = "idToActivity")
    @Mapping(target = "eventType",
            source = "eventTypeId",
            qualifiedByName = "idToEvent")
    SubscriptionOfferEntity toEntity(SubscriptionOfferCreateDTO dto,
                                     @Context ServiceRepository serviceRepository,
                                     @Context EventTypeRepository eventTypeRepository);


    @Mapping(target = "termOfValidityInDays", qualifiedByName = "intToPeriod")
    @Mapping(target = "activities", source = "activitiesId",
            qualifiedByName = "idToActivity")
    @Mapping(target = "eventType",
            source = "eventTypeId",
            qualifiedByName = "idToEvent")
    SubscriptionOfferEntity toEntity(SubscriptionOfferDTO dto,
                                     @Context ServiceRepository serviceRepository,
                                     @Context EventTypeRepository eventTypeRepository);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "activities", source = "activitiesId",
            qualifiedByName = "idToActivity")
    @Mapping(target = "eventType",
            source = "eventTypeId",
            qualifiedByName = "idToEvent")
    void updateFromDto(SubscriptionOfferDTO dto,
                       @MappingTarget SubscriptionOfferEntity entity,
                       @Context ServiceRepository serviceRepository,
                       @Context EventTypeRepository eventTypeRepository);

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

    @Named("eventToId")
    static Long eventToId(EventTypeEntity entity) {
        return entity != null ? entity.getId() : null;
    }

    @Named("idToEvent")
    default EventTypeEntity idToEvent(Long id,
                                      @Context EventTypeRepository repository) {
        return repository.findById(id)
                .orElseThrow(() -> new EventTypeNotFoundException("Event type not found: " + id));
    }
}
