package ua.shpp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class EventTypeService {

    private final EventTypeRepository eventTypeRepository;
    private final EventTypeMapper eventTypeMapper;
    private final OneTimeOfferMapper oneTimeOfferMapper;
    private final SubscriptionOfferMapper subscriptionOfferMapper;

    public ResponseEntity<EventTypeResponseDTO> create(EventTypeRequestDTO eventTypeRequestDTO) {
        if (eventTypeRepository.existsByName(eventTypeRequestDTO.name())) {
            throw new EventTypeAlreadyExistsException("Event type with name "
                    + eventTypeRequestDTO.name() + " already exists.");
        }

        EventTypeEntity entity = eventTypeMapper.toEntity(eventTypeRequestDTO);

        // Ініціалізуємо списки, щоб уникнути NullPointerException
        if (entity.getOneTimeVisits() == null) {
            entity.setOneTimeVisits(new ArrayList<>());
        }
        if (entity.getSubscriptions() == null) {
            entity.setSubscriptions(new ArrayList<>());
        }

        // Мапінг і додавання в колекції (якщо є)
        if (eventTypeRequestDTO.oneTimeVisits() != null) {
            entity.getOneTimeVisits().addAll(
                    eventTypeRequestDTO.oneTimeVisits().stream()
                            .map(oneTimeOfferMapper::dtoToEntity)
                            .peek(e -> e.setEventType(entity))
                            .toList()
            );
        }

        if (eventTypeRequestDTO.subscriptions() != null) {
            entity.getSubscriptions().addAll(
                    eventTypeRequestDTO.subscriptions().stream()
                            .map(subscriptionOfferMapper::toEntity)
                            .peek(e -> e.setEventType(entity))
                            .toList()
            );
        }

        EventTypeEntity saved = eventTypeRepository.save(entity);

        return new ResponseEntity<>(eventTypeMapper.toResponseDTO(saved), HttpStatus.CREATED);
    }

    public ResponseEntity<EventTypeResponseDTO> get(Long id) {
        EventTypeEntity entity = eventTypeRepository.findById(id)
                .orElseThrow(() -> new EventTypeNotFoundException("Event type with id " + id + " not found"));

        return new ResponseEntity<>(eventTypeMapper.toResponseDTO(entity), HttpStatus.OK);
    }

    public ResponseEntity<EventTypeResponseDTO> update(Long id, EventTypeRequestDTO eventTypeRequestDTO) {
        EventTypeEntity existing = eventTypeRepository.findById(id)
                .orElseThrow(() -> new EventTypeNotFoundException("Event type with id " + id + " not found"));

        if (eventTypeRepository.existsByName(eventTypeRequestDTO.name())) {
            throw new EventTypeAlreadyExistsException("Event type with name "
                    + eventTypeRequestDTO.name() + " already exists.");
        }

        existing.setName(eventTypeRequestDTO.name());

        existing.getOneTimeVisits().clear();
        existing.getSubscriptions().clear();

        // Додавання нових послуг та абонементів
        if (eventTypeRequestDTO.oneTimeVisits() != null) {
            eventTypeRequestDTO.oneTimeVisits().forEach(service -> {
                OneTimeServiceEntity serviceEntity = oneTimeOfferMapper.dtoToEntity(service);
                serviceEntity.setEventType(existing);
                existing.getOneTimeVisits().add(serviceEntity);
            });
        }

        if (eventTypeRequestDTO.subscriptions() != null) {
            eventTypeRequestDTO.subscriptions().forEach(service -> {
                SubscriptionServiceEntity subscriptionEntity = subscriptionOfferMapper.toEntity(service);
                subscriptionEntity.setEventType(existing);
                existing.getSubscriptions().add(subscriptionEntity);
            });
        }

        EventTypeEntity updated = eventTypeRepository.save(existing);
        return new ResponseEntity<>(eventTypeMapper.toResponseDTO(updated), HttpStatus.OK);
    }

    public ResponseEntity<Void> delete(Long id) {
        eventTypeRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}

