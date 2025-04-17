package ua.shpp.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ua.shpp.dto.OneTimeOfferDTO;
import ua.shpp.entity.ServiceEntity;
import ua.shpp.exception.OfferNotFoundException;
import ua.shpp.exception.ServiceNotFoundException;
import ua.shpp.mapper.OneTimeOfferMapper;
import ua.shpp.repository.OneTimeOfferRepository;
import ua.shpp.repository.ServiceRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class OneTimeOfferService {
    private final OneTimeOfferMapper oneTimeOfferMapper;
    private final OneTimeOfferRepository oneTimeOfferRepository;
    private final ServiceRepository serviceRepository;

    public OneTimeOfferDTO create(OneTimeOfferDTO oneTimeOfferDTO) {
        log.debug("create() called with DTO: {}", oneTimeOfferDTO);
        var entity = oneTimeOfferMapper.dtoToEntity(oneTimeOfferDTO,serviceRepository);
        entity.setId(null);
        ServiceEntity serviceEntity = serviceRepository.findById(oneTimeOfferDTO.activity())
                .orElseThrow(() -> new ServiceNotFoundException("Service not found"));
        entity.setActivity(serviceEntity);
        entity = oneTimeOfferRepository.save(entity);
        log.info("Created one-time offer (id={})", entity.getId());
        return oneTimeOfferMapper.entityToDto(entity);
    }

    public OneTimeOfferDTO get(Long id) {
        log.debug("get() called with id: {}", id);
        var entity = oneTimeOfferRepository.findById(id)
                .orElseThrow(() -> new OfferNotFoundException(String.format("Offer id: %d, not found", id)));
        log.info("Fetching one-time offer (id={})", id);
        return oneTimeOfferMapper.entityToDto(entity);
    }

    public OneTimeOfferDTO update(OneTimeOfferDTO updateDto) {
        log.debug("update() called with DTO: {}", updateDto);
        var entity = oneTimeOfferRepository.findById(updateDto.id())
                .orElseThrow(() -> new OfferNotFoundException(String.format("Offer id: %d, not found", updateDto.id())));
        oneTimeOfferMapper.updateFromDto(updateDto, entity,serviceRepository);
        oneTimeOfferRepository.save(entity);
        log.info("Updated one-time offer (id={})", entity.getId());
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
