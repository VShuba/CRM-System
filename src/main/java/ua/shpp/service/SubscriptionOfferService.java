package ua.shpp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ua.shpp.dto.SubscriptionOfferDTO;
import ua.shpp.exception.OfferNotFoundException;
import ua.shpp.mapper.SubscriptionOfferMapper;
import ua.shpp.repository.EventTypeRepository;
import ua.shpp.repository.ServiceRepository;
import ua.shpp.repository.SubscriptionOfferRepository;


@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionOfferService {
    private final SubscriptionOfferRepository subscriptionOfferRepository;
    private final SubscriptionOfferMapper subscriptionOfferMapper;
    private final ServiceRepository serviceRepository;
    private final EventTypeRepository eventTypeRepository;

    public SubscriptionOfferDTO create(SubscriptionOfferDTO dto) {
        log.debug("create() called with DTO: {}", dto);
        var entity = subscriptionOfferMapper
                .toEntity(dto, serviceRepository, eventTypeRepository);
        entity.setId(null);
        entity = subscriptionOfferRepository.save(entity);
        log.info("Created subscription offer (id={})", entity.getId());
        log.debug("Created subscription entity: {}", entity);
        return subscriptionOfferMapper.toDto(entity);
    }

    public SubscriptionOfferDTO getById(Long id) {
        log.debug("get() called with id: {}", id);
        var entity = subscriptionOfferRepository.findById(id)
                .orElseThrow(() -> new OfferNotFoundException(
                        String.format("Offer id: %d, not found", id)));
        log.info("Fetching subscription offer (id={})", id);
        log.debug("Fetching subscription offer entity: {}", entity);
        return subscriptionOfferMapper.toDto(entity);
    }

    public Page<SubscriptionOfferDTO> getAll(int page, int size, String sort, Boolean sortAsc){
        Sort.Order order = Sort.Order.by(sort);
        Pageable pageable = Boolean.TRUE.equals(sortAsc)
                ? PageRequest.of(page, size, Sort.by(order).ascending())
                : PageRequest.of(page, size, Sort.by(order).descending());
        return subscriptionOfferRepository
                .findAll(pageable)
                .map(subscriptionOfferMapper::toDto);
    }

    public SubscriptionOfferDTO update(SubscriptionOfferDTO updateDto) {
        log.debug("update() called with DTO: {}", updateDto);
        var entity = subscriptionOfferRepository.findById(updateDto.id())
                .orElseThrow(() -> new OfferNotFoundException(
                        String.format("Offer id: %d, not found", updateDto.id())));
        subscriptionOfferMapper.updateFromDto(updateDto, entity, serviceRepository, eventTypeRepository);
        subscriptionOfferRepository.save(entity);
        log.info("Updated subscription offer (id={})", entity.getId());
        log.info("Updated subscription offer entity: {}", entity);
        return subscriptionOfferMapper.toDto(entity);
    }

    public void delete(Long id) {
        log.debug("delete() called with id: {}", id);
        if (!subscriptionOfferRepository.existsById(id)) {
            log.debug("Offer id: {}, not found", id);
        } else {
            subscriptionOfferRepository.deleteById(id);
            log.info("Deleted subscription offer (id={})", id);
        }
    }
}
