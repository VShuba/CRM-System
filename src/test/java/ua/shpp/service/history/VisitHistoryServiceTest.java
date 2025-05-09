package ua.shpp.service.history;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.shpp.dto.VisitHistoryDTO;
import ua.shpp.entity.EventClientEntity;
import ua.shpp.entity.EventClientId;
import ua.shpp.entity.VisitHistoryEntity;
import ua.shpp.exception.ClientNotFoundException;
import ua.shpp.mapper.VisitHistoryMapper;
import ua.shpp.repository.ClientRepository;
import ua.shpp.repository.VisitHistoryRepository;
import ua.shpp.model.PaymentMethodForStory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VisitHistoryServiceTest {

    @Mock
    private VisitHistoryRepository visitHistoryRepository;

    @Mock
    private VisitHistoryMapper visitHistoryMapper;

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private VisitHistoryService visitHistoryService;

    private static final Long CLIENT_ID = 1L;
    private static final Long OTHER_CLIENT_ID = 2L;
    private static final Long NON_EXISTENT_CLIENT_ID = 99L;

    private static final Long SAVED_ENTITY_ID = 100L;
    private static final Long HISTORY_ENTITY_ID = 1L;

    private static final String SERVICE_NAME = "Yoga";
    private static final String SERVICE_COLOR = "#FF0000";
    private static final String TRAINER_NAME = "Ivan Petren";
    private static final String ROOM_NAME = "Main hall";
    private static final PaymentMethodForStory DEFAULT_PAYMENT_METHOD = PaymentMethodForStory.CASH;

    private static final LocalDate HISTORY_VISIT_DATE = LocalDate.of(2025, 4, 27);
    private static final LocalTime HISTORY_VISIT_TIME = LocalTime.of(15, 30);
    private static final BigDecimal HISTORY_AMOUNT = new BigDecimal("120.00");

    private static final LocalDate CREATE_TEST_DATE = LocalDate.of(2024, 4, 29);
    private static final LocalTime CREATE_TEST_TIME = LocalTime.of(10, 0);
    private static final BigDecimal CREATE_TEST_AMOUNT = new BigDecimal("150.00");

    private static final Long EVENT_ID = 2L;

    private EventClientEntity createEventClientEntity() {
        EventClientEntity eventClient = new EventClientEntity();
        eventClient.setEventUserId(new EventClientId(CLIENT_ID, EVENT_ID));
        return eventClient;
    }

    private VisitHistoryEntity createVisitHistoryEntity(Long id, LocalDate date, LocalTime time, BigDecimal amount) {
        return VisitHistoryEntity.builder()
                .id(id)
                .serviceName(SERVICE_NAME)
                .date(date)
                .time(time)
                .serviceColor(SERVICE_COLOR)
                .amountPaid(amount)
                .paymentMethodForStory(DEFAULT_PAYMENT_METHOD)
                .trainerFullName(TRAINER_NAME)
                .roomName(ROOM_NAME)
                .build();
    }

    private VisitHistoryEntity createMappedVisitHistoryEntity() {
        return createVisitHistoryEntity(
                null,
                CREATE_TEST_DATE,
                CREATE_TEST_TIME,
                CREATE_TEST_AMOUNT
        );
    }

    private VisitHistoryEntity createSavedVisitHistoryEntity() {
        return createVisitHistoryEntity(
                SAVED_ENTITY_ID,
                CREATE_TEST_DATE,
                CREATE_TEST_TIME,
                CREATE_TEST_AMOUNT
        );
    }

    private VisitHistoryEntity createHistoryVisitHistoryEntity() {
        return createVisitHistoryEntity(
                HISTORY_ENTITY_ID,
                HISTORY_VISIT_DATE,
                HISTORY_VISIT_TIME,
                HISTORY_AMOUNT
        );
    }

    private VisitHistoryDTO createHistoryVisitHistoryDTO() {
        return new VisitHistoryDTO(
                HISTORY_ENTITY_ID,
                CLIENT_ID,
                SERVICE_COLOR,
                SERVICE_NAME,
                TRAINER_NAME,
                HISTORY_VISIT_DATE,
                HISTORY_VISIT_TIME,
                ROOM_NAME,
                DEFAULT_PAYMENT_METHOD,
                HISTORY_AMOUNT
        );
    }

    @Test
    void createVisitHistoryEntry_validEventClient_shouldReturnSavedEntity() {
        // Arrange
        EventClientEntity eventClient = createEventClientEntity();
        VisitHistoryEntity mappedEntity = createMappedVisitHistoryEntity();
        VisitHistoryEntity savedEntity = createSavedVisitHistoryEntity();

        when(visitHistoryMapper.toVisitHistoryEntity(eventClient)).thenReturn(mappedEntity);
        when(visitHistoryRepository.save(mappedEntity)).thenReturn(savedEntity);

        // Act
        VisitHistoryEntity result = visitHistoryService.createVisitHistoryEntry(eventClient);

        // Assert
        assertNotNull(result, "The result must not be null");
        assertEquals(SAVED_ENTITY_ID, result.getId(), "The saved entity must have an assigned ID");
        assertEquals(SERVICE_NAME, result.getServiceName(), "The service name must match");
        assertEquals(DEFAULT_PAYMENT_METHOD, result.getPaymentMethodForStory(), "Payment method must match");
        assertEquals(CREATE_TEST_DATE, result.getDate(), "The date of the visit must coincide");
        assertEquals(CREATE_TEST_TIME, result.getTime(), "The time of the visit must coincide");
        assertEquals(CREATE_TEST_AMOUNT.stripTrailingZeros(), result.getAmountPaid().stripTrailingZeros(),
                "The amount paid must match");
        assertEquals(SERVICE_COLOR, result.getServiceColor(), "The color of the service must match");
        assertEquals(TRAINER_NAME, result.getTrainerFullName(), "Coach name must match");
        assertEquals(ROOM_NAME, result.getRoomName(), "Room name must match");

        // Verify
        verify(visitHistoryMapper).toVisitHistoryEntity(eventClient);
        verify(visitHistoryRepository).save(mappedEntity);
        verifyNoMoreInteractions(visitHistoryMapper, visitHistoryRepository);
        verifyNoInteractions(clientRepository);
    }

    @Test
    void getVisitHistoryByClientId_clientExists_shouldReturnDtoList() {
        // Arrange
        VisitHistoryEntity mockEntity = createHistoryVisitHistoryEntity();
        List<VisitHistoryEntity> historyEntities = Collections.singletonList(mockEntity);

        VisitHistoryDTO mockDto = createHistoryVisitHistoryDTO();
        List<VisitHistoryDTO> expectedDtoList = Collections.singletonList(mockDto);

        when(clientRepository.existsById(CLIENT_ID)).thenReturn(true);
        when(visitHistoryRepository.findAllByClientId(CLIENT_ID)).thenReturn(historyEntities);
        when(visitHistoryMapper.toDtoList(historyEntities)).thenReturn(expectedDtoList);

        // Act
        List<VisitHistoryDTO> result = visitHistoryService.getVisitHistoryByClientId(CLIENT_ID);

        // Assert
        assertNotNull(result, "The result must not be null");
        assertEquals(1, result.size(), "A DTO list must contain one element");

        VisitHistoryDTO dto = result.getFirst();
        assertAll(
                () -> assertEquals(HISTORY_ENTITY_ID, dto.id(), "DTO ID must match"),
                () -> assertEquals(CLIENT_ID, dto.clientId(), "DTO client ID must match"),
                () -> assertEquals(SERVICE_COLOR, dto.serviceColor(),
                        "The color of the DTO service must match"),
                () -> assertEquals(SERVICE_NAME, dto.serviceName(), "The DTO service name must match"),
                () -> assertEquals(TRAINER_NAME, dto.trainerFullName(), "DTO trainer name must match"),
                () -> assertEquals(HISTORY_VISIT_DATE, dto.date(), "The date of the DTO visit must coincide"),
                () -> assertEquals(HISTORY_VISIT_TIME, dto.time(),
                        "The time of the DTO visit should coincide"),
                () -> assertEquals(ROOM_NAME, dto.roomName(), "DTO room name must match"),
                () -> assertEquals(DEFAULT_PAYMENT_METHOD, dto.paymentMethodForStory(),
                        "DTO payment method must match"),
                () -> assertEquals(HISTORY_AMOUNT.stripTrailingZeros(), dto.amountPaid().stripTrailingZeros(),
                        "The DTO amount paid must match")
        );

        // Verify
        verify(clientRepository).existsById(CLIENT_ID);
        verify(visitHistoryRepository).findAllByClientId(CLIENT_ID);
        verify(visitHistoryMapper).toDtoList(historyEntities);
        verifyNoMoreInteractions(clientRepository, visitHistoryRepository, visitHistoryMapper);
    }

    @Test
    void getVisitHistoryByClientId_clientNotFound_shouldThrowClientNotFoundException() {
        // Arrange
        Long nonExistentClientId = NON_EXISTENT_CLIENT_ID;

        when(clientRepository.existsById(nonExistentClientId)).thenReturn(false);

        // Act & Assert
        assertThrows(ClientNotFoundException.class,
                () -> visitHistoryService.getVisitHistoryByClientId(nonExistentClientId),
                "ClientNotFoundException should be thrown");

        // Verify
        verify(clientRepository).existsById(nonExistentClientId);
        verifyNoInteractions(visitHistoryRepository);
        verifyNoInteractions(visitHistoryMapper);
        verifyNoMoreInteractions(clientRepository);
    }

    @Test
    void getVisitHistoryByClientId_clientExistsButNoHistory_shouldReturnEmptyList() {
        // Arrange
        Long clientIdWithNoHistory = OTHER_CLIENT_ID;

        when(clientRepository.existsById(clientIdWithNoHistory)).thenReturn(true);

        List<VisitHistoryEntity> emptyHistoryList = Collections.emptyList();
        when(visitHistoryRepository.findAllByClientId(clientIdWithNoHistory)).thenReturn(emptyHistoryList);

        List<VisitHistoryDTO> emptyDtoList = Collections.emptyList();
        when(visitHistoryMapper.toDtoList(emptyHistoryList)).thenReturn(emptyDtoList);

        // Act
        List<VisitHistoryDTO> result = visitHistoryService.getVisitHistoryByClientId(clientIdWithNoHistory);

        // Assert
        assertNotNull(result, "The result must not be null");
        assertTrue(result.isEmpty(), "The results list must be empty");

        // Verify
        verify(clientRepository).existsById(clientIdWithNoHistory);
        verify(visitHistoryRepository).findAllByClientId(clientIdWithNoHistory);
        verify(visitHistoryMapper).toDtoList(emptyHistoryList);
        verifyNoMoreInteractions(clientRepository, visitHistoryRepository, visitHistoryMapper);
    }
}