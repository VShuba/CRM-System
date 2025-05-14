package ua.shpp.service.history;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ua.shpp.dto.SubscriptionHistoryDTO;
import ua.shpp.entity.ClientEntity;
import ua.shpp.entity.SubscriptionHistoryEntity;
import ua.shpp.entity.SubscriptionOfferEntity;
import ua.shpp.entity.payment.SubscriptionDealEntity;
import ua.shpp.exception.ClientNotFoundException;
import ua.shpp.exception.MissingSubscriptionServiceException;
import ua.shpp.exception.SubscriptionHistoryCreationException;
import ua.shpp.mapper.SubscriptionHistoryMapper;
import ua.shpp.repository.ClientRepository;
import ua.shpp.repository.SubscriptionHistoryRepository;
import ua.shpp.repository.SubscriptionDealRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionHistoryService {
    private final SubscriptionHistoryRepository subscriptionHistoryRepository;
    private final SubscriptionHistoryMapper subscriptionHistoryMapper;
    private final ClientRepository clientRepository;
    private final SubscriptionDealRepository subscriptionDealRepository;

    public void createSubscriptionHistory(SubscriptionDealEntity subscriptionInfo) {
        log.info("Creating history for SubscriptionInfo ID: {}", subscriptionInfo.getId());
        SubscriptionOfferEntity subscriptionService = subscriptionInfo.getSubscriptionService();

        if (subscriptionInfo.getSubscriptionService() == null) {
            throw new MissingSubscriptionServiceException(subscriptionInfo.getId());
        }

        try {
            SubscriptionHistoryEntity historyEntity = subscriptionHistoryMapper.toHistory(subscriptionInfo,
                    subscriptionService);
            subscriptionHistoryRepository.save(historyEntity);
            log.info("Created history ID: {} for SubscriptionInfo ID: {}", historyEntity.getId(),
                    subscriptionInfo.getId());
        } catch (Exception e) {
            log.error("Error creating history for SubscriptionInfo ID: {}: {}", subscriptionInfo.getId(),
                    e.getMessage(), e);
            throw new SubscriptionHistoryCreationException("Failed to save history for ID: " +
                    subscriptionInfo.getId(), e);
        }
    }

    public List<SubscriptionHistoryDTO> getSubscriptionHistoryByClientAndValidity(Long clientId, Boolean isValid) {
        log.info("Fetching history for client ID: {}, isValid: {}", clientId, isValid);

        log.debug("Validating client existence before filtered history fetch, clientId={}", clientId);
        clientRepository.findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException(clientId));

        log.debug("Fetching subscription history entities from repository for client ID: {} and isValid: {}",
                clientId, isValid);
        List<SubscriptionHistoryEntity> historyEntities =
                subscriptionHistoryRepository.findByClientIdAndIsValid(clientId, isValid);

        log.info("Found {} records for client ID: {}, isValid: {}", historyEntities.size(),
                clientId, isValid);

        List<SubscriptionHistoryDTO> historyDTOs = historyEntities.stream()
                .map(subscriptionHistoryMapper::toDto)
                .toList();

        log.info("Successfully fetched and mapped subscription history for client ID: {}, {}",
                clientId, historyDTOs.size());
        return historyDTOs;
    }

    public List<SubscriptionHistoryDTO> getAllSubscriptionHistoryByClient(Long clientId) {
        log.info("Fetching all history for client ID: {}", clientId);

        log.debug("Checking existence for client ID: {}", clientId);
        clientRepository.findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException(clientId));

        log.debug("Fetching all subscription history entities for client ID: {}", clientId);
        List<SubscriptionHistoryEntity> historyEntities = subscriptionHistoryRepository.findByClientId(clientId);
        log.info("Found {} total records for client ID: {}", historyEntities.size(), clientId);

        log.debug("Mapping {} entities to DTOs", historyEntities.size());
        List<SubscriptionHistoryDTO> historyDTOs = historyEntities.stream()
                .map(subscriptionHistoryMapper::toDto)
                .toList();

        log.info("Successfully fetched and mapped all subscription history for client ID: {}, {}",
                clientId, historyDTOs.size());
        return historyDTOs;
    }

    public void updateHistoryVisitsRemaining(SubscriptionDealEntity subscriptionInfo) {
        log.info("Updating visits_left for SubscriptionInfo ID: {}", subscriptionInfo.getId());

        SubscriptionOfferEntity subscriptionService = subscriptionInfo.getSubscriptionService();
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
                    !subscriptionInfo.getExpirationDate().isBefore(LocalDate.now());

            historyEntityToUpdate.setIsValid(newIsValid);

            subscriptionHistoryRepository.save(historyEntityToUpdate);
            log.info("Successfully updated visits_left to {} in history record with ID: {} " +
                            "for SubscriptionInfo ID: {} (Type: {}, Name: {})",
                    subscriptionInfo.getVisits(), historyEntityToUpdate.getId(), subscriptionInfo.getId(),
                    historyEntityToUpdate.getEventType(), historyEntityToUpdate.getName());
        } else {
            log.error("No history found for SubscriptionInfo ID: {}, clientId={}, name={}, type={}, isValid={}",
                    subscriptionInfo.getId(), clientId, subscriptionOfferName, eventType, isValid);
            throw new IllegalStateException("No history record found to update for SubscriptionInfo ID: " +
                    subscriptionInfo.getId());
        }
    }

    public void validateActiveSubscriptions() {
        log.info("Starting daily subscription validation...");

        List<SubscriptionHistoryEntity> histories = subscriptionHistoryRepository.findAll();

        for (SubscriptionHistoryEntity history : histories) {
            Long clientId = history.getClient().getId();
            String name = history.getName();
            String eventType = history.getEventType();

            Optional<SubscriptionDealEntity> subscriptionInfoOpt = subscriptionDealRepository
                            .findByClientIdAndSubscriptionService_NameAndSubscriptionService_EventType_Name(
                            clientId, name, eventType);

            if (subscriptionInfoOpt.isPresent()) {
                SubscriptionDealEntity subscriptionInfo = subscriptionInfoOpt.get();

                boolean shouldBeValid = subscriptionInfo.getVisits() > 0 &&
                        !subscriptionInfo.getExpirationDate().isBefore(LocalDate.now());

                if (!shouldBeValid && Boolean.TRUE.equals(history.getIsValid())) {
                    history.setIsValid(false);
                    subscriptionHistoryRepository.save(history);
                    log.info("Marked subscription invalid. History ID: {}", history.getId());
                }
            } else {
                log.warn("No matching SubscriptionInfo found for history ID: {}", history.getId());
            }
        }

        log.info("Finished subscription validation.");
    }
}
