package ua.shpp.service;

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
import ua.shpp.model.ClientEventStatus;
import ua.shpp.repository.ClientRepository;
import ua.shpp.repository.EventClientRepository;
import ua.shpp.repository.ScheduleEventRepository;

@Service
public class EventClientService {

    private final EventClientRepository eventClientRepository;
    private final ClientRepository clientRepository;
    private final ScheduleEventRepository scheduleEventRepository;
    private final ClientService clientService;

    public EventClientService(EventClientRepository eventClientRepository, ClientRepository clientRepository, ScheduleEventRepository scheduleEventRepository, ClientService clientService) {
        this.eventClientRepository = eventClientRepository;
        this.clientRepository = clientRepository;
        this.scheduleEventRepository = scheduleEventRepository;
        this.clientService = clientService;
    }


    public EventClientDto addClientToEvent(Long clientId, Long eventId) {

        ClientEntity clientEntity = clientRepository.findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException(clientId));
        ScheduleEventEntity scheduleEvent = scheduleEventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        EventClientEntity eventClientEntity = EventClientEntity.builder()
                .eventUserId(new EventClientId(clientId, eventId))
                .client(clientEntity)
                .scheduleEvent(scheduleEvent)
                .clientEventStatus(ClientEventStatus.ASSIGNED)
                .build();

        EventClientEntity savedEventClient = eventClientRepository.save(eventClientEntity);
        return new EventClientDto(
                savedEventClient.getEventUserId().getClientId(),
                savedEventClient.getEventUserId().getEventId(),
                savedEventClient.getClientEventStatus()
        );
    }

    public EventClientDto changeClientStatus(EventClientDto eventClientDto) {
        EventClientId eventClientId =
                new EventClientId(eventClientDto.clientId(), eventClientDto.scheduleId());
        EventClientEntity eventClientEntity = eventClientRepository.findById(eventClientId)
                .orElseThrow(() -> new EventForClientNotFoundException(eventClientDto.clientId(), eventClientDto.scheduleId()));
        if (ClientEventStatus.checkIfStatusChangePossible(eventClientEntity.getClientEventStatus(), eventClientDto.clientEventStatus())) {
            eventClientEntity.setClientEventStatus(eventClientDto.clientEventStatus());
            EventClientEntity savedEventClient = eventClientRepository.save(eventClientEntity);
            return new EventClientDto(
                    savedEventClient.getEventUserId().getClientId(),
                    savedEventClient.getEventUserId().getEventId(),
                    savedEventClient.getClientEventStatus()
            );
        } else throw new ClientEventStatusChnageException();
    }


    public EventClientDto addClientAndAssignEvent(Long eventId, Long orgId, ClientRequestDto eventClientDto) {
        ClientResponseDto client = clientService.createClient(orgId, eventClientDto);
        return addClientToEvent(client.id(), eventId);
    }
}
