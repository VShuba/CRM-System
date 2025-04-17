package ua.shpp.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ua.shpp.dto.OneTimeOfferDTO;
import ua.shpp.entity.OneTimeServiceEntity;
import ua.shpp.exception.OfferNotFoundException;
import ua.shpp.mapper.OneTimeOfferMapper;
import ua.shpp.repository.OneTimeOfferRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class OneTimeOfferService {
    private final OneTimeOfferMapper oneTimeOfferMapper;
    private final OneTimeOfferRepository oneTimeOfferRepository;

    public OneTimeOfferDTO create(OneTimeOfferDTO oneTimeOfferDTO) {
        log.debug("create() called with DTO: {}", oneTimeOfferDTO);
        var entity = oneTimeOfferMapper.dtoToEntity(oneTimeOfferDTO);
        entity.setId(null);
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
        var oldDto = get(updateDto.id());
        var updateEntity = updateEntity(oldDto, updateDto);
        oneTimeOfferRepository.save(updateEntity);
        log.info("Updated one-time offer (id={})", updateEntity.getId());
        return oneTimeOfferMapper.entityToDto(updateEntity);
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

    private OneTimeServiceEntity updateEntity(OneTimeOfferDTO oldDto, OneTimeOfferDTO updateDto) {
        OneTimeServiceEntity updateEntity = new OneTimeServiceEntity();
        updateEntity.setId(updateDto.id());
        if (updateDto.durationInMinutes() != null) {
            updateEntity.setDurationInMinutes(updateDto.durationInMinutes());
        } else {
            updateEntity.setDurationInMinutes(oldDto.durationInMinutes());
        }
        if (updateDto.price() != null) {
            updateEntity.setPrice(updateDto.price());
        } else {
            updateEntity.setPrice(oldDto.price());
        }
        //todo add  Activity blocker
        return updateEntity;
    }
}
