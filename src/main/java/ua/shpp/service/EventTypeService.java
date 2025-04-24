package ua.shpp.service;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.shpp.dto.EventTypeRequestDTO;
import ua.shpp.dto.EventTypeResponseDTO;
import ua.shpp.entity.EventTypeEntity;
import ua.shpp.exception.BranchNotFoundException;
import ua.shpp.exception.EventTypeAlreadyExistsException;
import ua.shpp.exception.EventTypeNotFoundException;
import ua.shpp.mapper.EventTypeMapper;
import ua.shpp.mapper.OneTimeOfferMapper;
import ua.shpp.mapper.SubscriptionOfferMapper;
import ua.shpp.repository.BranchRepository;
import ua.shpp.repository.EventTypeRepository;
import ua.shpp.repository.ServiceRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventTypeService {

    private final EventTypeRepository eventTypeRepository;
    private final EventTypeMapper eventTypeMapper;
    private final OneTimeOfferMapper oneTimeOfferMapper;
    private final SubscriptionOfferMapper subscriptionOfferMapper;
    private final ServiceRepository serviceRepository;
    private final BranchRepository branchRepository;

    public EventTypeResponseDTO create(EventTypeRequestDTO dto) {
        log.info("Creating EventType: {}", dto.name());

        validateUniqueEventType(dto.name(), dto.branchId());

        EventTypeEntity entity = eventTypeMapper.toEntity(dto, serviceRepository, branchRepository);
        addOffersToEventType(dto, entity);

        EventTypeEntity saved = eventTypeRepository.save(entity);
        log.info("Created EventType with ID: {}", saved.getId());

        return eventTypeMapper.toResponseDTO(saved);
    }

    public EventTypeResponseDTO getById(Long id) {
        log.info("Fetching EventType with ID: {}", id);
        EventTypeEntity entity = eventTypeRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("EventType not found: {}", id);
                    return new EventTypeNotFoundException("Event type with id " + id + " not found");
                });
        return eventTypeMapper.toResponseDTO(entity);
    }

    public List<EventTypeResponseDTO> getAllByBranch(Long branchId) {
        log.info("Fetching EventTypes for branch ID: {}", branchId);
        return eventTypeRepository.findAllByBranchId(branchId).stream()
                .map(eventTypeMapper::toResponseDTO)
                .toList();
    }

    public EventTypeResponseDTO update(Long id, EventTypeRequestDTO dto) {
        log.info("Updating EventType with ID: {}", id);

        EventTypeEntity existing = eventTypeRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("EventType not found: {}", id);
                    return new EventTypeNotFoundException("Event type with id " + id + " not found");
                });

        validateNameChangeUniqueness(existing, dto);

        updateEntityFromDTO(existing, dto);
        addOffersToEventType(dto, existing);

        EventTypeEntity updated = eventTypeRepository.save(existing);
        log.info("Updated EventType with ID: {}", updated.getId());

        return eventTypeMapper.toResponseDTO(updated);
    }

    public void delete(Long id) {
        log.info("Deleting EventType with ID: {}", id);

        if (!eventTypeRepository.existsById(id)) {
            log.warn("EventType not found: {}", id);
            throw new EventTypeNotFoundException("Event type with id " + id + " not found");
        }

        eventTypeRepository.deleteById(id);
        log.info("Deleted EventType with ID: {}", id);
    }

    public Page<EventTypeResponseDTO> getFiltered(String name, Long branchId, Long serviceId, Pageable pageable) {
        Page<EventTypeEntity> page = eventTypeRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null && !name.isBlank()) {
                Predicate namePredicate = cb.like(
                        cb.lower(root.get("name").as(String.class)),
                        "%" + name.toLowerCase() + "%"
                );
                predicates.add(namePredicate);
            }

            if (branchId != null) {
                predicates.add(cb.equal(root.get("branch").get("id"), branchId));
            }

            if (serviceId != null) {
                Join<EventTypeEntity, ?> oneTimeJoin = root.join("oneTimeVisits", JoinType.LEFT);
                Join<EventTypeEntity, ?> subscriptionJoin = root.join("subscriptions", JoinType.LEFT);
                predicates.add(cb.or(
                        cb.equal(oneTimeJoin.get("service").get("id"), serviceId),
                        cb.equal(subscriptionJoin.get("service").get("id"), serviceId)
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);

        return page.map(eventTypeMapper::toResponseDTO);
    }


    private void validateUniqueEventType(String name, Long branchId) {
        if (eventTypeRepository.existsByNameAndBranchId(name, branchId)) {
            log.warn("Duplicate EventType: '{}' in branch '{}'", name, branchId);
            throw new EventTypeAlreadyExistsException("Event type with name " + name
                    + " already exists in this branch");
        }
    }

    private void validateNameChangeUniqueness(EventTypeEntity existing, EventTypeRequestDTO dto) {
        boolean nameChanged = !existing.getName().equals(dto.name());
        boolean branchChanged = !existing.getBranch().getId().equals(dto.branchId());

        if ((nameChanged || branchChanged)
                && eventTypeRepository.existsByNameAndBranchId(dto.name(), dto.branchId())) {
            log.warn("Duplicate EventType on update: '{}' in branch '{}'", dto.name(), dto.branchId());
            throw new EventTypeAlreadyExistsException("Event type with name " + dto.name()
                    + " already exists in this branch");
        }
    }

    private void updateEntityFromDTO(EventTypeEntity entity, EventTypeRequestDTO dto) {
        entity.setName(dto.name());
        entity.setBranch(branchRepository.findById(dto.branchId())
                .orElseThrow(() -> new BranchNotFoundException("Branch not found with id: " + dto.branchId())));
        entity.getOneTimeVisits().clear();
        entity.getSubscriptions().clear();
    }

    private void addOffersToEventType(EventTypeRequestDTO eventTypeRequestDTO, EventTypeEntity eventTypeEntity) {
        if (eventTypeRequestDTO.oneTimeVisits() != null) {
            eventTypeRequestDTO.oneTimeVisits().stream()
                    .map(oneTimeVisitDTO -> {
                        var oneTimeVisitEntity = oneTimeOfferMapper
                                .dtoToEntity(oneTimeVisitDTO, serviceRepository, eventTypeRepository);
                        oneTimeVisitEntity.setEventType(eventTypeEntity);
                        return oneTimeVisitEntity;
                    })
                    .forEach(eventTypeEntity.getOneTimeVisits()::add);
        }

        if (eventTypeRequestDTO.subscriptions() != null) {
            eventTypeRequestDTO.subscriptions().stream()
                    .map(subscriptionDTO -> {
                        var subscriptionEntity = subscriptionOfferMapper
                                .toEntity(subscriptionDTO, serviceRepository, eventTypeRepository);
                        subscriptionEntity.setEventType(eventTypeEntity);
                        return subscriptionEntity;
                    })
                    .forEach(eventTypeEntity.getSubscriptions()::add);
        }
    }
}

