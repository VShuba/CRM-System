package ua.shpp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.shpp.dto.ClientRequestDto;
import ua.shpp.dto.ClientResponseDto;
import ua.shpp.dto.EventClientDto;
import ua.shpp.entity.ClientEntity;
import ua.shpp.entity.EventClientEntity;
import ua.shpp.entity.EventClientId;
import ua.shpp.entity.payment.OneTimeInfoEntity;
import ua.shpp.entity.payment.SubscriptionInfoEntity;
import ua.shpp.exception.ClientEventStatusChnageException;
import ua.shpp.exception.ClientNotFoundException;
import ua.shpp.exception.EventForClientNotFoundException;
import ua.shpp.exception.EventNotFoundException;
import ua.shpp.model.ClientEventStatus;
import ua.shpp.repository.ClientRepository;
import ua.shpp.repository.ScheduleEventRepository;
import ua.shpp.repository.EventClientRepository;
import ua.shpp.mapper.EventClientMapper;
import ua.shpp.entity.ScheduleEventEntity;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;

@ExtendWith(MockitoExtension.class)
class EventClientServiceTest {
    @Mock
    private EventClientRepository eventClientRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ScheduleEventRepository scheduleEventRepository;

    @Mock
    private ClientService clientService;

    @Mock
    private VisitHistoryService visitHistoryService;

    @Mock
    private EventClientMapper eventClientMapper;

    @InjectMocks
    private EventClientService eventClientService;
    private ClientEntity clientEntity;
    private ScheduleEventEntity scheduleEventEntity;
    private OneTimeInfoEntity oneTimeInfoEntity;
    private SubscriptionInfoEntity subscriptionInfoEntity;

    private EventClientEntity baseEventClientEntity;
    private EventClientDto baseEventClientDto;
    private EventClientId eventClientId;
    private ClientRequestDto baseClientRequestDto;
    private ClientResponseDto baseClientResponseDto;


    private final Long clientId = 1L;
    private final Long eventId = 10L;
    private final Long orgId = 100L;
    private final Long oneTimeInfoId = 50L;
    private final Long subscriptionInfoId = 60L;


    @BeforeEach
    void setUp() {
        clientEntity = new ClientEntity();
        clientEntity.setId(clientId);
        clientEntity.setName("Test Client");
        clientEntity.setPhone("0991112233");
        clientEntity.setBirthday(LocalDate.now().minusYears(25));
        clientEntity.setComment("Test comment");

        scheduleEventEntity = new ScheduleEventEntity();
        scheduleEventEntity.setId(eventId);

        eventClientId = new EventClientId(clientId, eventId);

        oneTimeInfoEntity = OneTimeInfoEntity.builder().id(oneTimeInfoId).client(clientEntity).build();
        subscriptionInfoEntity = SubscriptionInfoEntity.builder().id(subscriptionInfoId).client(clientEntity).build();

        baseEventClientEntity = EventClientEntity.builder()
                .eventUserId(eventClientId)
                .client(clientEntity)
                .scheduleEvent(scheduleEventEntity)
                .clientEventStatus(ClientEventStatus.ASSIGNED)
                .oneTimeInfo(null)
                .subscriptionInfo(null)
                .build();

        baseEventClientDto = new EventClientDto(
                clientId, eventId, ClientEventStatus.ASSIGNED, null, null
        );

        baseClientRequestDto = new ClientRequestDto(
                "New Test Client", "0994445566", LocalDate.now().minusYears(30),
                "New client comment"
        );
        baseClientResponseDto = new ClientResponseDto(
                clientId,
                clientEntity.getName(),
                clientEntity.getPhone(),
                clientEntity.getBirthday(),
                clientEntity.getComment()
        );
    }

    @Test
    void addClientToEvent_Success() {
        // Arrange
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(clientEntity));
        when(scheduleEventRepository.findById(eventId)).thenReturn(Optional.of(scheduleEventEntity));
        when(eventClientRepository.save(any(EventClientEntity.class))).thenAnswer(invocation -> {
            EventClientEntity entityToSave = invocation.getArgument(0);
            assertEquals(ClientEventStatus.ASSIGNED, entityToSave.getClientEventStatus());
            assertNull(entityToSave.getOneTimeInfo());
            assertNull(entityToSave.getSubscriptionInfo());
            return EventClientEntity.builder()
                    .eventUserId(entityToSave.getEventUserId())
                    .client(entityToSave.getClient())
                    .scheduleEvent(entityToSave.getScheduleEvent())
                    .clientEventStatus(ClientEventStatus.ASSIGNED)
                    .oneTimeInfo(null)
                    .subscriptionInfo(null)
                    .build();
        });
        when(eventClientMapper.toDto(any(EventClientEntity.class))).thenAnswer(invocation -> {
            EventClientEntity entity = invocation.getArgument(0);
            assertNull(entity.getOneTimeInfo());
            assertNull(entity.getSubscriptionInfo());
            return new EventClientDto(
                    entity.getEventUserId().getClientId(),
                    entity.getEventUserId().getEventId(),
                    entity.getClientEventStatus(),
                    null,
                    null
            );
        });

        // Act
        EventClientDto result = eventClientService.addClientToEvent(clientId, eventId);

        // Assert
        assertNotNull(result);
        assertEquals(clientId, result.clientId());
        assertEquals(eventId, result.scheduleId());
        assertEquals(ClientEventStatus.ASSIGNED, result.clientEventStatus());
        assertNull(result.oneTimeInfoId());
        assertNull(result.subscriptionInfoId());

        verify(clientRepository).findById(clientId);
        verify(scheduleEventRepository).findById(eventId);
        verify(eventClientRepository).save(argThat(entity ->
                entity.getClient() == clientEntity &&
                        entity.getScheduleEvent() == scheduleEventEntity &&
                        entity.getClientEventStatus() == ClientEventStatus.ASSIGNED &&
                        entity.getOneTimeInfo() == null &&
                        entity.getSubscriptionInfo() == null));
        verify(eventClientMapper).toDto(argThat(entity ->
                entity.getOneTimeInfo() == null && entity.getSubscriptionInfo() == null));
        verifyNoMoreInteractions(clientRepository, scheduleEventRepository, eventClientRepository,
                eventClientMapper, clientService, visitHistoryService);
    }

    @Test
    void addClientToEvent_ClientNotFound_ThrowsException() {
        // Arrange
        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());
        // Act & Assert
        assertThrows(ClientNotFoundException.class, () -> eventClientService.addClientToEvent(clientId, eventId));
        // Verify
        verify(clientRepository).findById(clientId);
        verifyNoInteractions(scheduleEventRepository, eventClientRepository, eventClientMapper,
                clientService, visitHistoryService);
    }

    @Test
    void addClientToEvent_EventNotFound_ThrowsException() {
        // Arrange
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(clientEntity));
        when(scheduleEventRepository.findById(eventId)).thenReturn(Optional.empty());
        // Act & Assert
        assertThrows(EventNotFoundException.class, () -> eventClientService.addClientToEvent(clientId, eventId));
        // Verify
        verify(clientRepository).findById(clientId);
        verify(scheduleEventRepository).findById(eventId);
        verifyNoInteractions(eventClientRepository, eventClientMapper, clientService, visitHistoryService);
    }

    // Допоміжний метод для налаштування моків для успішної зміни статусу
    private void setupChangeStatusMocks(EventClientEntity foundEntity, EventClientEntity savedEntity) {
        when(eventClientRepository.findById(eventClientId)).thenReturn(Optional.of(foundEntity));
        when(eventClientRepository.save(any(EventClientEntity.class))).thenReturn(savedEntity);
        when(eventClientMapper.toDto(eq(savedEntity))).thenAnswer(invocation -> {
            EventClientEntity entity = invocation.getArgument(0);
            Long otId = (entity.getOneTimeInfo() != null) ? entity.getOneTimeInfo().getId() : null;
            Long subId = (entity.getSubscriptionInfo() != null) ? entity.getSubscriptionInfo().getId() : null;
            return new EventClientDto(
                    entity.getEventUserId().getClientId(),
                    entity.getEventUserId().getEventId(),
                    entity.getClientEventStatus(),
                    otId,
                    subId
            );
        });

        when(visitHistoryService.createVisitHistoryEntry(any(EventClientEntity.class))).thenReturn(null);
    }

    // Допоміжний метод для перевірки взаємодій при успішній зміні статусу
    private void verifyStatusChangeInteractions(EventClientEntity foundEntity, EventClientEntity savedEntity) {
        verify(eventClientRepository).findById(eventClientId);
        verify(eventClientRepository).save(argThat(entity ->
                entity.getClientEventStatus() == savedEntity.getClientEventStatus() &&
                        entity.getOneTimeInfo() == foundEntity.getOneTimeInfo() &&
                        entity.getSubscriptionInfo() == foundEntity.getSubscriptionInfo()
        ));
        verify(eventClientMapper).toDto(eq(savedEntity));
        verify(visitHistoryService).createVisitHistoryEntry(eq(savedEntity));
        verifyNoMoreInteractions(eventClientRepository, eventClientMapper, visitHistoryService,
                clientRepository, scheduleEventRepository, clientService);
    }


    @Test
    void changeClientStatus_Success_AssignedToUsed() {
        // Arrange
        ClientEventStatus oldStatus = ClientEventStatus.ASSIGNED;
        ClientEventStatus newStatus = ClientEventStatus.USED;

        EventClientEntity foundEntity = EventClientEntity.builder()
                .eventUserId(eventClientId).client(clientEntity).scheduleEvent(scheduleEventEntity)
                .clientEventStatus(oldStatus)
                .oneTimeInfo(oneTimeInfoEntity)
                .subscriptionInfo(null)
                .build();

        EventClientEntity savedEntity = EventClientEntity.builder()
                .eventUserId(eventClientId).client(clientEntity).scheduleEvent(scheduleEventEntity)
                .clientEventStatus(newStatus)
                .oneTimeInfo(foundEntity.getOneTimeInfo())
                .subscriptionInfo(foundEntity.getSubscriptionInfo())
                .build();

        EventClientDto inputDto = new EventClientDto(clientId, eventId, newStatus,
                null, null);

        setupChangeStatusMocks(foundEntity, savedEntity);

        // Act
        EventClientDto result = eventClientService.changeClientStatus(inputDto);

        // Assert
        assertNotNull(result);
        assertEquals(newStatus, result.clientEventStatus());
        assertEquals(oneTimeInfoId, result.oneTimeInfoId());
        assertNull(result.subscriptionInfoId());

        verifyStatusChangeInteractions(foundEntity, savedEntity);
    }

    @Test
    void changeClientStatus_Success_AssignedToSkipped() {
        // Arrange
        ClientEventStatus oldStatus = ClientEventStatus.ASSIGNED;
        ClientEventStatus newStatus = ClientEventStatus.SKIPPED;

        EventClientEntity foundEntity = EventClientEntity.builder()
                .eventUserId(eventClientId).client(clientEntity).scheduleEvent(scheduleEventEntity)
                .clientEventStatus(oldStatus)
                .oneTimeInfo(null)
                .subscriptionInfo(subscriptionInfoEntity)
                .build();

        EventClientEntity savedEntity = EventClientEntity.builder()
                .eventUserId(eventClientId).client(clientEntity).scheduleEvent(scheduleEventEntity)
                .clientEventStatus(newStatus)
                .oneTimeInfo(foundEntity.getOneTimeInfo())
                .subscriptionInfo(foundEntity.getSubscriptionInfo())
                .build();

        EventClientDto inputDto = new EventClientDto(clientId, eventId, newStatus,
                null, null);

        setupChangeStatusMocks(foundEntity, savedEntity);

        // Act
        EventClientDto result = eventClientService.changeClientStatus(inputDto);

        // Assert
        assertNotNull(result);
        assertEquals(newStatus, result.clientEventStatus());
        assertNull(result.oneTimeInfoId());
        assertEquals(subscriptionInfoId, result.subscriptionInfoId());

        verifyStatusChangeInteractions(foundEntity, savedEntity);
    }


    @Test
    void changeClientStatus_EventClientNotFound_ThrowsException() {
        // Arrange
        EventClientDto inputDto = new EventClientDto(clientId, eventId, ClientEventStatus.USED,
                null, null);
        when(eventClientRepository.findById(eventClientId)).thenReturn(Optional.empty());
        // Act & Assert
        assertThrows(EventForClientNotFoundException.class, () -> eventClientService.changeClientStatus(inputDto));
        // Verify
        verify(eventClientRepository).findById(eventClientId);

        verifyNoMoreInteractions(eventClientRepository, eventClientMapper, visitHistoryService,
                clientRepository, scheduleEventRepository, clientService);
    }

    @Test
    void changeClientStatus_InvalidTransition_FromUsed_ThrowsException() {
        // Arrange
        ClientEventStatus oldStatus = ClientEventStatus.USED;
        ClientEventStatus newStatus = ClientEventStatus.ASSIGNED;
        EventClientDto inputDto = new EventClientDto(clientId, eventId, newStatus,
                null, null);
        EventClientEntity foundEntity = EventClientEntity.builder().clientEventStatus(oldStatus).build();
        when(eventClientRepository.findById(eventClientId)).thenReturn(Optional.of(foundEntity));

        // Act & Assert
        ClientEventStatusChnageException exception = assertThrows(ClientEventStatusChnageException.class,
                () -> eventClientService.changeClientStatus(inputDto));
        assertEquals(String.format("Status change not possible from %s to %s", oldStatus, newStatus),
                exception.getMessage());
        // Verify
        verify(eventClientRepository).findById(eventClientId);

        verifyNoMoreInteractions(eventClientRepository, eventClientMapper, visitHistoryService,
                clientRepository, scheduleEventRepository, clientService);
    }

    @Test
    void changeClientStatus_InvalidTransition_FromSkipped_ThrowsException() {
        // Arrange
        ClientEventStatus oldStatus = ClientEventStatus.SKIPPED;
        ClientEventStatus newStatus = ClientEventStatus.USED;
        EventClientDto inputDto = new EventClientDto(clientId, eventId, newStatus,
                null, null);
        EventClientEntity foundEntity = EventClientEntity.builder().clientEventStatus(oldStatus).build();
        when(eventClientRepository.findById(eventClientId)).thenReturn(Optional.of(foundEntity));

        // Act & Assert
        ClientEventStatusChnageException exception = assertThrows(ClientEventStatusChnageException.class,
                () -> eventClientService.changeClientStatus(inputDto));
        assertEquals(String.format("Status change not possible from %s to %s", oldStatus, newStatus),
                exception.getMessage());
        // Verify
        verify(eventClientRepository).findById(eventClientId);

        verifyNoMoreInteractions(eventClientRepository, eventClientMapper, visitHistoryService,
                clientRepository, scheduleEventRepository, clientService);
    }

    @Test
    void changeClientStatus_InvalidTransition_SameStatus_ThrowsException() {
        // Arrange
        ClientEventStatus oldStatus = ClientEventStatus.ASSIGNED;
        ClientEventStatus newStatus = ClientEventStatus.ASSIGNED;
        EventClientDto inputDto = new EventClientDto(clientId, eventId, newStatus,
                null, null);
        EventClientEntity foundEntity = EventClientEntity.builder().clientEventStatus(oldStatus).build();
        when(eventClientRepository.findById(eventClientId)).thenReturn(Optional.of(foundEntity));

        // Act & Assert
        ClientEventStatusChnageException exception = assertThrows(ClientEventStatusChnageException.class,
                () -> eventClientService.changeClientStatus(inputDto));
        assertEquals(String.format("Status change not possible from %s to %s", oldStatus, newStatus),
                exception.getMessage());
        // Verify
        verify(eventClientRepository).findById(eventClientId);

        verifyNoMoreInteractions(eventClientRepository, eventClientMapper, visitHistoryService,
                clientRepository, scheduleEventRepository, clientService);
    }

    @Test
    void addClientAndAssignEvent_Success() {
        // Arrange
        Long newClientId = 2L;

        ClientRequestDto newClientRequestDto = new ClientRequestDto(
                "Brand New Client", "0997778899", LocalDate.now().minusYears(40),
                "Comment for new client"
        );

        ClientResponseDto createdClientResponseDto = new ClientResponseDto(
                newClientId,
                newClientRequestDto.name(),
                newClientRequestDto.phone(),
                newClientRequestDto.birthday(),
                newClientRequestDto.comment()
        );

        when(clientService.createClient(orgId, newClientRequestDto)).thenReturn(createdClientResponseDto);

        ClientEntity newClientEntity = new ClientEntity();
        newClientEntity.setId(newClientId);
        newClientEntity.setName(createdClientResponseDto.name());
        newClientEntity.setPhone(createdClientResponseDto.phone());
        newClientEntity.setBirthday(createdClientResponseDto.birthday());
        newClientEntity.setComment(createdClientResponseDto.comment());
        when(clientRepository.findById(newClientId)).thenReturn(Optional.of(newClientEntity));

        when(scheduleEventRepository.findById(eventId)).thenReturn(Optional.of(scheduleEventEntity));

        EventClientId newEventClientId = new EventClientId(newClientId, eventId);
        EventClientEntity savedNewEventClient = EventClientEntity.builder()
                .eventUserId(newEventClientId)
                .client(newClientEntity)
                .scheduleEvent(scheduleEventEntity)
                .clientEventStatus(ClientEventStatus.ASSIGNED)
                .oneTimeInfo(null)
                .subscriptionInfo(null)
                .build();
        when(eventClientRepository.save(any(EventClientEntity.class))).thenReturn(savedNewEventClient);

        EventClientDto expectedFinalDto = new EventClientDto(
                newClientId, eventId, ClientEventStatus.ASSIGNED, null, null
        );
        when(eventClientMapper.toDto(eq(savedNewEventClient))).thenReturn(expectedFinalDto);

        // Act
        EventClientDto result = eventClientService.addClientAndAssignEvent(eventId, orgId, newClientRequestDto);

        // Assert
        assertNotNull(result);
        assertEquals(newClientId, result.clientId());
        assertEquals(eventId, result.scheduleId());
        assertEquals(ClientEventStatus.ASSIGNED, result.clientEventStatus());
        assertNull(result.oneTimeInfoId());
        assertNull(result.subscriptionInfoId());

        verify(clientService).createClient(orgId, newClientRequestDto);
        verify(clientRepository).findById(newClientId);
        verify(scheduleEventRepository).findById(eventId);
        verify(eventClientRepository).save(argThat(entity ->
                entity.getClient() == newClientEntity &&
                        entity.getScheduleEvent() == scheduleEventEntity &&
                        entity.getClientEventStatus() == ClientEventStatus.ASSIGNED &&
                        entity.getOneTimeInfo() == null &&
                        entity.getSubscriptionInfo() == null
        ));
        verify(eventClientMapper).toDto(eq(savedNewEventClient));

        verifyNoMoreInteractions(clientService, clientRepository, scheduleEventRepository,
                eventClientRepository, eventClientMapper, visitHistoryService);
    }

    @Test
    void addClientAndAssignEvent_ClientCreationFails_ThrowsException() {
        // Arrange
        ClientRequestDto requestDto = new ClientRequestDto("Fail Client", "0991112233",
                null, null);
        RuntimeException creationException = new RuntimeException("DB error");

        when(clientService.createClient(orgId, requestDto)).thenThrow(creationException);

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
                eventClientService.addClientAndAssignEvent(eventId, orgId, requestDto));

        // Verify
        verify(clientService).createClient(orgId, requestDto);

        verifyNoInteractions(clientRepository, scheduleEventRepository, eventClientRepository,
                eventClientMapper, visitHistoryService);
    }

    @Test
    void addClientAndAssignEvent_EventNotFoundAfterClientCreation_ThrowsException() {
        // Arrange
        Long newClientId = 3L;
        ClientRequestDto requestDto = new ClientRequestDto("Temp Client", "0993334455",
                null, null);
        ClientResponseDto createdClientDto = new ClientResponseDto(newClientId, requestDto.name(), requestDto.phone(), requestDto.birthday(), requestDto.comment()); // Новий DTO
        ClientEntity newClientEntity = new ClientEntity();
        newClientEntity.setId(newClientId);

        when(clientService.createClient(orgId, requestDto)).thenReturn(createdClientDto);
        when(clientRepository.findById(newClientId)).thenReturn(Optional.of(newClientEntity));
        when(scheduleEventRepository.findById(eventId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EventNotFoundException.class, () ->
                eventClientService.addClientAndAssignEvent(eventId, orgId, requestDto));

        // Verify
        verify(clientService).createClient(orgId, requestDto);
        verify(clientRepository).findById(newClientId);
        verify(scheduleEventRepository).findById(eventId);

        verifyNoInteractions(eventClientRepository, eventClientMapper, visitHistoryService);
    }
}