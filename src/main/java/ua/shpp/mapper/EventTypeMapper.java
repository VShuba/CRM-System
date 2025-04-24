package ua.shpp.mapper;

import org.mapstruct.*;
import ua.shpp.dto.EventTypeRequestDTO;
import ua.shpp.dto.EventTypeResponseDTO;
import ua.shpp.entity.BranchEntity;
import ua.shpp.entity.EventTypeEntity;
import ua.shpp.entity.ServiceEntity;
import ua.shpp.exception.BranchNotFoundException;
import ua.shpp.exception.ServiceNotFoundException;
import ua.shpp.repository.BranchRepository;
import ua.shpp.repository.ServiceRepository;

import java.time.Duration;
import java.time.Period;
import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {OneTimeOfferMapper.class, SubscriptionOfferMapper.class}
)
public interface EventTypeMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "oneTimeVisits", source = "oneTimeVisits")
    @Mapping(target = "subscriptions", source = "subscriptions")
    @Mapping(target = "branch", expression = "java(resolveBranch(dto.branchId(), branchRepository))")
    EventTypeEntity toEntity(EventTypeRequestDTO dto,
                             @Context ServiceRepository serviceRepository,
                             @Context BranchRepository branchRepository);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "oneTimeVisits", source = "oneTimeVisits")
    @Mapping(target = "subscriptions", source = "subscriptions")
    @Mapping(target = "branchName", source = "branch.name")
    EventTypeResponseDTO toResponseDTO(EventTypeEntity entity);

    // --- default methods ---

    default BranchEntity resolveBranch(Long branchId, BranchRepository branchRepository) {
        if (branchId == null) return null;
        return branchRepository.findById(branchId)
                .orElseThrow(() -> new BranchNotFoundException("Branch not found with id: " + branchId));
    }

    default Duration map(Long minutes) {
        return minutes != null ? Duration.ofMinutes(minutes) : null;
    }

    default Period map(Integer days) {
        return days != null ? Period.ofDays(days) : null;
    }

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
