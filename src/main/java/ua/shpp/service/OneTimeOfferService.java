package ua.shpp.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.shpp.dto.OneTimeOfferCreateDTO;
import ua.shpp.dto.OneTimeOfferDTO;
import ua.shpp.exception.OfferNotFoundException;
import ua.shpp.mapper.OneTimeOfferMapper;
import ua.shpp.repository.EventTypeRepository;
import ua.shpp.repository.OneTimeOfferRepository;
import ua.shpp.repository.ServiceRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class OneTimeOfferService {
    private final OneTimeOfferMapper oneTimeOfferMapper;
    private final OneTimeOfferRepository oneTimeOfferRepository;
    private final ServiceRepository serviceRepository;
    private final EventTypeRepository eventTypeRepository;

    public OneTimeOfferDTO create(OneTimeOfferCreateDTO oneTimeOfferDTO) {
        log.debug("create() called with DTO: {}", oneTimeOfferDTO);
        var entity = oneTimeOfferMapper
                .dtoToEntity(oneTimeOfferDTO, serviceRepository, eventTypeRepository);
        entity = oneTimeOfferRepository.save(entity);
        log.info("Created one-time offer (id={})", entity.getId());
        log.debug("create() one-time offer Entity: {}", entity);
        return oneTimeOfferMapper.entityToDto(entity);
    }

    public OneTimeOfferDTO getById(Long id) {
        log.debug("get() called with id: {}", id);
        var entity = oneTimeOfferRepository.findById(id)
                .orElseThrow(() -> new OfferNotFoundException(
                        String.format("Offer id: %d, not found", id)));
        log.info("Fetching one-time offer (id={})", id);
        log.debug("Fetching one-time offer entity: {}", entity);
        return oneTimeOfferMapper.entityToDto(entity);
    }

    public Page<OneTimeOfferDTO> getAllByEventTypeId(Long eventTypeId, Pageable pageRequest){
        log.info("getAllByEventTypeId() called with id: {}", eventTypeId);
        var allOffers = oneTimeOfferRepository
                .findAllByEventTypeId(eventTypeId, pageRequest);
        log.debug("Fetching all one-time offer entity: {}", allOffers);
        return allOffers.map(oneTimeOfferMapper::entityToDto);
    }

    public OneTimeOfferDTO update(OneTimeOfferDTO updateDto) {
        log.debug("update() called with DTO: {}", updateDto);
        var entity = oneTimeOfferRepository.findById(updateDto.id())
                .orElseThrow(() -> new OfferNotFoundException(
                        String.format("Offer id: %d, not found", updateDto.id())));
        oneTimeOfferMapper.updateFromDto(updateDto, entity,serviceRepository, eventTypeRepository);
        oneTimeOfferRepository.save(entity);
        log.info("Updated one-time offer (id={})", entity.getId());
        log.debug("Updated one-time offer entity: {}", entity);
        return oneTimeOfferMapper.entityToDto(entity);
    }

    public void delete(Long id) {
        log.debug("delete() called with id: {}", id);
        if (!oneTimeOfferRepository.existsById(id)) {
            log.debug("Offer id: {}, not found", id);
        } else {
            oneTimeOfferRepository.deleteById(id);
            log.info("Deleted one-time offer (id={})", id);
        }
    }
}
