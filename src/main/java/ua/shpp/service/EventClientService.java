package ua.shpp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ua.shpp.dto.ClientRequestDto;
import ua.shpp.dto.ClientResponseDto;
import ua.shpp.dto.EventClientDto;
import ua.shpp.entity.ClientEntity;
import ua.shpp.entity.EventClientEntity;
import ua.shpp.entity.EventClientId;
import ua.shpp.entity.ScheduleEventEntity;
import ua.shpp.exception.ClientEventStatusChnageException;
import ua.shpp.exception.ClientNotFoundException;
import ua.shpp.exception.EventForClientNotFoundException;
import ua.shpp.exception.EventNotFoundException;
import ua.shpp.mapper.EventClientMapper;
import ua.shpp.model.ClientEventStatus;
import ua.shpp.repository.*;
import ua.shpp.service.history.VisitHistoryService;

@Service
@Slf4j
public class EventClientService {
    private final EventClientRepository eventClientRepository;
    private final ClientRepository clientRepository;
    private final ScheduleEventRepository scheduleEventRepository;
    private final ClientService clientService;
    private final VisitHistoryService visitHistoryService;
    private final EventClientMapper eventClientMapper;
    private final OneTimeDealRepository oneTimeDealRepository;
    private final SubscriptionDealRepository subscriptionDealRepository;

    public EventClientService(EventClientRepository eventClientRepository, ClientRepository clientRepository,
                              ScheduleEventRepository scheduleEventRepository, ClientService clientService,
                              VisitHistoryService visitHistoryService, EventClientMapper eventClientMapper,
                              OneTimeDealRepository oneTimeDealRepository,
                              SubscriptionDealRepository subscriptionDealRepository) {
        this.eventClientRepository = eventClientRepository;
        this.clientRepository = clientRepository;
        this.scheduleEventRepository = scheduleEventRepository;
        this.clientService = clientService;
        this.visitHistoryService = visitHistoryService;
        this.eventClientMapper = eventClientMapper;
        this.oneTimeDealRepository = oneTimeDealRepository;
        this.subscriptionDealRepository = subscriptionDealRepository;
    }

    public EventClientDto addClientToEvent(Long clientId, Long eventId) {
        log.info("Attempting to add client {} to event {}", clientId, eventId);

        ClientEntity clientEntity = clientRepository.findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException(clientId));

        ScheduleEventEntity scheduleEvent = scheduleEventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));

        EventClientEntity eventClientEntity = EventClientEntity.builder()
                .eventUserId(new EventClientId(clientId, eventId))
                .client(clientEntity)
                .scheduleEvent(scheduleEvent)
                .clientEventStatus(ClientEventStatus.ASSIGNED)
                .oneTimeInfo(null)
                .subscriptionInfo(null)
                .build();

        EventClientEntity savedEventClient = eventClientRepository.save(eventClientEntity);
        log.info("Client {} assigned to event {} with status {}", clientId, eventId,
                savedEventClient.getClientEventStatus());

        return eventClientMapper.toDto(savedEventClient);
    }

    public EventClientDto changeClientStatus(EventClientDto eventClientDto) {
        log.debug("Attempting to change status for client event: {}", eventClientDto);
        EventClientId eventClientId = new EventClientId(eventClientDto.clientId(), eventClientDto.scheduleId());

        EventClientEntity eventClientEntity = eventClientRepository.findById(eventClientId)
                .orElseThrow(() -> {
                    log.warn("EventClient not found for client {} and event {}", eventClientDto.clientId(),
                            eventClientDto.scheduleId());
                    return new EventForClientNotFoundException(eventClientDto.clientId(),
                            eventClientDto.scheduleId());
                });

        ClientEventStatus oldStatus = eventClientEntity.getClientEventStatus();
        ClientEventStatus newStatus = eventClientDto.clientEventStatus();

        if (ClientEventStatus.checkIfStatusChangePossible(oldStatus, newStatus)) {
            log.info("Changing status for client {} on event {} from {} to {}",
                    eventClientDto.clientId(), eventClientDto.scheduleId(),
                    oldStatus, newStatus);

            eventClientEntity.setClientEventStatus(newStatus);

            if (eventClientDto.oneTimeInfoId() != null) {
                eventClientEntity.setOneTimeInfo(oneTimeDealRepository
                        .findById(eventClientDto.oneTimeInfoId()).orElseThrow());
            } else if(eventClientDto.subscriptionInfoId() != null) {
                eventClientEntity.setSubscriptionInfo(subscriptionDealRepository
                        .findById(eventClientDto.subscriptionInfoId()).orElseThrow());
            }

            EventClientEntity savedEventClient = eventClientRepository.save(eventClientEntity);
            log.info("Status changed successfully for client {} on event {} to {}",
                    savedEventClient.getEventUserId().getClientId(),
                    savedEventClient.getEventUserId().getEventId(),
                    savedEventClient.getClientEventStatus());

            if (savedEventClient.getClientEventStatus() == ClientEventStatus.USED ||
                    savedEventClient.getClientEventStatus() == ClientEventStatus.SKIPPED) {
                log.debug("Status is USED or SKIPPED, creating visit history entry.");
                visitHistoryService.createVisitHistoryEntry(savedEventClient);
            }

            return eventClientMapper.toDto(savedEventClient);
        } else {
            log.warn("Status change not possible for client {} on event {} from {} to {}." +
                            " Current status order: {}, New status order: {}",
                    eventClientDto.clientId(), eventClientDto.scheduleId(), oldStatus, newStatus,
                    oldStatus.getOrder(), newStatus.getOrder());
            throw new ClientEventStatusChnageException(String.format("Status change not possible from %s to %s",
                    oldStatus, newStatus));
        }
    }

    public EventClientDto addClientAndAssignEvent(Long eventId, Long orgId, ClientRequestDto eventClientDto) {
        log.debug("Attempting to add client and assign to event {}. Org ID: {}", eventId, orgId);
        ClientResponseDto client = clientService.createClient(orgId, eventClientDto);
        log.info("Client created with ID: {}", client.id());
        return addClientToEvent(client.id(), eventId);
    }
}
