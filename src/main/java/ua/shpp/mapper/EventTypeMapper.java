package ua.shpp.mapper;

import org.mapstruct.*;
import ua.shpp.dto.EventTypeRequestDTO;
import ua.shpp.dto.EventTypeResponseDTO;
import ua.shpp.entity.*;
import ua.shpp.exception.BranchNotFoundException;
import ua.shpp.exception.ServiceNotFoundException;
import ua.shpp.repository.BranchRepository;
import ua.shpp.repository.OneTimeOfferRepository;
import ua.shpp.repository.ServiceRepository;
import ua.shpp.repository.SubscriptionOfferRepository;

import java.time.Duration;
import java.time.Period;
import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface EventTypeMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "oneTimeVisits", source = "oneTimeVisits",
            qualifiedByName = "idToOneTimeVisits")
    @Mapping(target = "subscriptions", source = "subscriptions",
            qualifiedByName = "idToSubscriptions")
    @Mapping(target = "branch", expression = "java(resolveBranch(dto.branchId(), branchRepository))")
    EventTypeEntity toEntity(EventTypeRequestDTO dto,
                             @Context ServiceRepository serviceRepository,
                             @Context BranchRepository branchRepository,
                             @Context OneTimeOfferRepository oneTimeOfferRepository,
                             @Context SubscriptionOfferRepository subscriptionOfferRepository
    );

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "oneTimeVisits", source = "oneTimeVisits",
            qualifiedByName = "oneTimeVisitsToId")
    @Mapping(target = "subscriptions", source = "subscriptions",
            qualifiedByName = "subscriptionsToId")
    @Mapping(target = "branchName", source = "branch.name")
    EventTypeResponseDTO toResponseDTO(EventTypeEntity entity);

    // --- default methods ---

    @Named("idToOneTimeVisits")
    default List<OneTimeServiceEntity> idToOneTimeVisits(List<Long> listId,
                                                         @Context OneTimeOfferRepository repository) {
        return repository.findAllById(listId);
    }

    @Named("idToSubscriptions")
    default List<SubscriptionServiceEntity> idToSubscriptions(List<Long> listId,
                                                              @Context SubscriptionOfferRepository repository) {
        return repository.findAllById(listId);
    }

    @Named("oneTimeVisitsToId")
    default List<Long> oneTimeVisitsToId(List<OneTimeServiceEntity> listEntity) {
        return listEntity.stream().map(OneTimeServiceEntity::getId).toList();
    }

    @Named("subscriptionsToId")
    static List<Long> subscriptionsToId(List<SubscriptionServiceEntity> listEntity) {
        return listEntity.stream().map(SubscriptionServiceEntity::getId).toList();
    }


    default BranchEntity resolveBranch(Long branchId, BranchRepository branchRepository) {
        if (branchId == null) return null;
        return branchRepository.findById(branchId)
                .orElseThrow(() -> new BranchNotFoundException("Branch not found with id: " + branchId));
    }

//    default Duration map(Long minutes) {
//        return minutes != null ? Duration.ofMinutes(minutes) : null;
//    }
//
//    default Period map(Integer days) {
//        return days != null ? Period.ofDays(days) : null;
//    }

    default ServiceEntity mapToServiceEntity(Long id, @Context ServiceRepository repository) {
        return repository.findById(id)
                .orElseThrow(() -> new ServiceNotFoundException("Service not found: " + id));
    }

    default List<ServiceEntity> mapToServiceEntityList(List<Long> ids, @Context ServiceRepository repository) {
        if (ids == null) return List.of();
        return ids.stream()
                .map(id -> repository.findById(id)
                        .orElseThrow(() -> new ServiceNotFoundException("Service not found: " + id)))
                .toList();
    }
}
