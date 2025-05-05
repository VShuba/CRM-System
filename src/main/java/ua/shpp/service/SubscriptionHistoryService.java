package ua.shpp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ua.shpp.dto.SubscriptionHistoryDTO;
import ua.shpp.entity.ClientEntity;
import ua.shpp.entity.SubscriptionHistoryEntity;
import ua.shpp.entity.SubscriptionServiceEntity;
import ua.shpp.entity.payment.SubscriptionInfoEntity;
import ua.shpp.exception.ClientNotFoundException;
import ua.shpp.mapper.SubscriptionHistoryMapper;
import ua.shpp.repository.ClientRepository;
import ua.shpp.repository.SubscriptionHistoryRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionHistoryService {

    private final SubscriptionHistoryRepository subscriptionHistoryRepository;
    private final SubscriptionHistoryMapper subscriptionHistoryMapper;
    private final ClientRepository clientRepository;

    public void createSubscriptionHistory(SubscriptionInfoEntity subscriptionInfo) {
        log.info("Attempting to create subscription history record for SubscriptionInfo ID: {}",
                subscriptionInfo.getId());

        SubscriptionServiceEntity subscriptionService = subscriptionInfo.getSubscriptionService();

        if (subscriptionService == null) {
            log.error("FATAL: SubscriptionService is null for SubscriptionInfo ID: {}. Cannot create history.",
                    subscriptionInfo.getId());
            throw new IllegalStateException("SubscriptionService is null for SubscriptionInfo ID: " +
                    subscriptionInfo.getId() + ". History cannot be created.");
        }

        try {
            SubscriptionHistoryEntity historyEntity = subscriptionHistoryMapper.toHistory(subscriptionInfo,
                    subscriptionService);
            subscriptionHistoryRepository.save(historyEntity);
            log.info("Successfully created SubscriptionHistory record with ID: {} for SubscriptionInfo ID: {}",
                    historyEntity.getId(), subscriptionInfo.getId());
        } catch (Exception e) {
            log.error("Failed to create subscription history record for SubscriptionInfo ID: {}. Error: {}",
                    subscriptionInfo.getId(), e.getMessage(), e);
            throw new RuntimeException("Failed to save subscription history for SubscriptionInfo ID: " +
                    subscriptionInfo.getId(), e);
        }
    }

    public List<SubscriptionHistoryDTO> getSubscriptionHistoryByClientAndValidity(Long clientId, Boolean isValid) {
        log.info("Attempting to fetch subscription history for client ID: {} with isValid status: {}",
                clientId, isValid);

        log.debug("Checking existence for client ID: {}", clientId);
        clientRepository.findById(clientId)
                .orElseThrow(() -> {
                    log.warn("Client with ID: {} not found. Throwing ClientNotFoundException.", clientId);
                    return new ClientNotFoundException(clientId); // Переконайся, що цей клас існує
                });
        log.debug("Client ID: {} found.", clientId);

        log.debug("Fetching subscription history entities from repository for client ID: {} and isValid: {}",
                clientId, isValid);
        List<SubscriptionHistoryEntity> historyEntities =
                subscriptionHistoryRepository.findByClientIdAndIsValid(clientId, isValid);

        log.info("Found {} subscription history entries for client ID: {} with isValid={}",
                historyEntities.size(), clientId, isValid);

        log.debug("Mapping {} entities to DTOs", historyEntities.size());
        List<SubscriptionHistoryDTO> historyDtos = historyEntities.stream()
                .map(subscriptionHistoryMapper::toDto)
                .toList();

        log.info("Successfully fetched and mapped subscription history for client ID: {}." +
                " Returning {} DTOs.", clientId, historyDtos.size());
        return historyDtos;
    }

    public List<SubscriptionHistoryDTO> getAllSubscriptionHistoryByClient(Long clientId) {
        log.info("Attempting to fetch all subscription history for client ID: {}", clientId);

        log.debug("Checking existence for client ID: {}", clientId);
        clientRepository.findById(clientId)
                .orElseThrow(() -> {
                    log.warn("Client with ID: {} not found.", clientId);
                    return new ClientNotFoundException(clientId);
                });
        log.debug("Client ID: {} found.", clientId);

        log.debug("Fetching all subscription history entities from repository for client ID: {}", clientId);
        List<SubscriptionHistoryEntity> historyEntities = subscriptionHistoryRepository.findByClientId(clientId);
        log.info("Found {} total subscription history entries for client ID: {}", historyEntities.size(), clientId);

        log.debug("Mapping {} entities to DTOs", historyEntities.size());
        List<SubscriptionHistoryDTO> historyDtos = historyEntities.stream()
                .map(subscriptionHistoryMapper::toDto)
                .toList();

        log.info("Successfully fetched and mapped all subscription history for client ID: {}." +
                " Returning {} DTOs.", clientId, historyDtos.size());
        return historyDtos;
    }

    public void updateHistoryVisitsRemaining(SubscriptionInfoEntity subscriptionInfo) {
        log.info("Attempting to update visits_left in history for SubscriptionInfo ID: {}", subscriptionInfo.getId());

        SubscriptionServiceEntity subscriptionService = subscriptionInfo.getSubscriptionService();
        ClientEntity client = subscriptionInfo.getClient();

        Long clientId = client.getId();
        String subscriptionOfferName = subscriptionService.getName();
        String eventType = subscriptionService.getEventType().getName();
        Boolean isValid = true;

        Optional<SubscriptionHistoryEntity> historyOptional =
                subscriptionHistoryRepository.findByClientIdAndNameAndEventTypeAndIsValid(clientId,
                        subscriptionOfferName, eventType, isValid);

        if (historyOptional.isPresent()) {
            SubscriptionHistoryEntity historyEntityToUpdate = historyOptional.get();

            historyEntityToUpdate.setVisitsLeft(subscriptionInfo.getVisits());

            boolean newIsValid = subscriptionInfo.getVisits() > 0 &&
                    subscriptionInfo.getExpirationDate().isAfter(LocalDate.now());

            historyEntityToUpdate.setIsValid(newIsValid);

            subscriptionHistoryRepository.save(historyEntityToUpdate);
            log.info("Successfully updated visits_left to {} in SubscriptionHistory record with ID: {} " +
                            "for SubscriptionInfo ID: {} (Type: {}, Name: {})",
                    subscriptionInfo.getVisits(), historyEntityToUpdate.getId(), subscriptionInfo.getId(),
                    historyEntityToUpdate.getEventType(), historyEntityToUpdate.getName());
        } else {
            log.error("FATAL: Could not find relevant history record for SubscriptionInfo ID:" +
                            " {} to update visits_left using criteria: clientId={}, name={}, eventType={}, isValid={}." +
                            " This indicates a data or logic mismatch.",
                    subscriptionInfo.getId(), clientId, subscriptionOfferName, eventType, isValid);

            throw new IllegalStateException("Could not find relevant history record for SubscriptionInfo ID: " +
                    subscriptionInfo.getId() + " to update.");
        }
    }
}
