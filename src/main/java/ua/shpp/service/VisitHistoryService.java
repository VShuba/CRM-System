package ua.shpp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ua.shpp.dto.VisitHistoryDTO;
import ua.shpp.entity.EventClientEntity;
import ua.shpp.entity.VisitHistoryEntity;
import ua.shpp.exception.ClientNotFoundException;
import ua.shpp.mapper.VisitHistoryMapper;
import ua.shpp.repository.ClientRepository;
import ua.shpp.repository.VisitHistoryRepository;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class VisitHistoryService {
    private final VisitHistoryRepository visitHistoryRepository;
    private final VisitHistoryMapper visitHistoryMapper;
    private final ClientRepository clientRepository;

    public VisitHistoryEntity createVisitHistoryEntry(EventClientEntity eventClientEntity) {
        log.debug("Creating visit history entry for EventClient with ID: {}", eventClientEntity.getEventUserId());

        VisitHistoryEntity visitHistoryEntity = visitHistoryMapper.toVisitHistoryEntity(eventClientEntity);

        VisitHistoryEntity savedEntity = visitHistoryRepository.save(visitHistoryEntity);
        log.info("Created visit history entry with ID: {}", savedEntity.getId());

        return savedEntity;
    }

    public List<VisitHistoryDTO> getVisitHistoryByClientId(Long clientId) {
        log.debug("Fetching visit history for client ID: {}", clientId);

        clientRepository.findById(clientId)
                .orElseThrow(() -> {
                    log.warn("Client with ID: {} not found.", clientId);
                    return new ClientNotFoundException(clientId);
                });
        log.debug("Client ID: {} found.", clientId);

        List<VisitHistoryEntity> historyEntities = visitHistoryRepository.findAllByClientId(clientId);

        log.info("Found {} history entries for client ID: {}", historyEntities.size(), clientId);
        return visitHistoryMapper.toDtoList(historyEntities);
    }
}


