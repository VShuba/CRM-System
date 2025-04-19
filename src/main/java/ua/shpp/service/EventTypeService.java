package ua.shpp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ua.shpp.dto.EventTypeRequestDTO;
import ua.shpp.dto.EventTypeResponseDTO;
import ua.shpp.entity.EventTypeEntity;
import ua.shpp.entity.OneTimeServiceEntity;
import ua.shpp.entity.SubscriptionServiceEntity;
import ua.shpp.exception.EventTypeAlreadyExistsException;
import ua.shpp.exception.EventTypeNotFoundException;
import ua.shpp.mapper.EventTypeMapper;
import ua.shpp.mapper.OneTimeOfferMapper;
import ua.shpp.mapper.SubscriptionOfferMapper;
import ua.shpp.repository.EventTypeRepository;
import ua.shpp.repository.ServiceRepository;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventTypeService {

    private final EventTypeRepository eventTypeRepository;
    private final EventTypeMapper eventTypeMapper;
    private final OneTimeOfferMapper oneTimeOfferMapper;
    private final SubscriptionOfferMapper subscriptionOfferMapper;
    private final ServiceRepository serviceRepository;

    public EventTypeResponseDTO create(EventTypeRequestDTO dto) {
        log.info("Creating new EventType with name: {}", dto.name());

        if (eventTypeRepository.existsByName(dto.name())) {
            log.warn("EventType with name '{}' already exists", dto.name());
            throw new EventTypeAlreadyExistsException("Event type with name " + dto.name() + " already exists");
        }

        EventTypeEntity entity = eventTypeMapper.toEntity(dto, serviceRepository);

        entity.setOneTimeVisits(Optional.ofNullable(entity.getOneTimeVisits()).orElseGet(ArrayList::new));
        entity.setSubscriptions(Optional.ofNullable(entity.getSubscriptions()).orElseGet(ArrayList::new));

        if (dto.oneTimeVisits() != null) {
            entity.getOneTimeVisits().addAll(
                    dto.oneTimeVisits().stream()
                            .map(x -> oneTimeOfferMapper
                                    .dtoToEntity(x, serviceRepository, eventTypeRepository))
                            .peek(e -> e.setEventType(entity))
                            .toList()
            );
        }

        if (dto.subscriptions() != null) {
            entity.getSubscriptions().addAll(
                    dto.subscriptions().stream()
                            .map(x -> subscriptionOfferMapper
                                    .toEntity(x, serviceRepository,eventTypeRepository))
                            .peek(e -> e.setEventType(entity))
                            .toList()
            );
        }

        EventTypeEntity saved = eventTypeRepository.save(entity);
        log.info("EventType created with id: {}", saved.getId());

        return eventTypeMapper.toResponseDTO(saved);
    }

    public EventTypeResponseDTO get(Long id) {
        log.info("Retrieving EventType with id: {}", id);
        EventTypeEntity entity = eventTypeRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("EventType with id '{}' not found", id);
                    return new EventTypeNotFoundException("Event type with id " + id + " not found");
                });

        return eventTypeMapper.toResponseDTO(entity);
    }

    public EventTypeResponseDTO update(Long id, EventTypeRequestDTO dto) {
        log.info("Updating EventType with id: {}", id);
        EventTypeEntity existing = eventTypeRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("EventType with id '{}' not found", id);
                    return new EventTypeNotFoundException("Event type with id " + id + " not found");
                });

        if (eventTypeRepository.existsByName(dto.name()) && !existing.getName().equals(dto.name())) {
            log.warn("EventType with name '{}' already exists", dto.name());
            throw new EventTypeAlreadyExistsException("Event type with name " + dto.name() + " already exists");
        }

        existing.setName(dto.name());
        existing.getOneTimeVisits().clear();
        existing.getSubscriptions().clear();

        if (dto.oneTimeVisits() != null) {
            dto.oneTimeVisits().forEach(service -> {
                OneTimeServiceEntity serviceEntity = oneTimeOfferMapper
                        .dtoToEntity(service, serviceRepository,eventTypeRepository);
                serviceEntity.setEventType(existing);
                existing.getOneTimeVisits().add(serviceEntity);
            });
        }

        if (dto.subscriptions() != null) {
            dto.subscriptions().forEach(service -> {
                SubscriptionServiceEntity subscriptionEntity = subscriptionOfferMapper
                        .toEntity(service, serviceRepository, eventTypeRepository);
                subscriptionEntity.setEventType(existing);
                existing.getSubscriptions().add(subscriptionEntity);
            });
        }

        EventTypeEntity updated = eventTypeRepository.save(existing);
        log.info("EventType updated with id: {}", updated.getId());

        return eventTypeMapper.toResponseDTO(updated);
    }

    public void delete(Long id) {
        log.info("Deleting EventType with id: {}", id);
        if (!eventTypeRepository.existsById(id)) {
            log.warn("EventType with id '{}' not found", id);
            throw new EventTypeNotFoundException("Event type with id " + id + " not found");
        }
        eventTypeRepository.deleteById(id);
        log.info("EventType with id '{}' deleted", id);
    }
}

