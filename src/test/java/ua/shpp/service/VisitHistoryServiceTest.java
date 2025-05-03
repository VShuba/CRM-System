package ua.shpp.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.shpp.dto.VisitHistoryDTO;
import ua.shpp.entity.EventClientEntity;
import ua.shpp.entity.EventClientId;
import ua.shpp.entity.VisitHistoryEntity;
import ua.shpp.mapper.VisitHistoryMapper;
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

    @InjectMocks
    private VisitHistoryService visitHistoryService;

    private static final Long CLIENT_ID = 1L;
    private static final String SERVICE_NAME = "Йога";
    private static final String SERVICE_COLOR = "#FF0000";
    private static final String TRAINER_NAME = "Іван Петренко";
    private static final String ROOM_NAME = "Головний зал";
    private static final LocalDate VISIT_DATE = LocalDate.of(2025, 4, 27);
    private static final LocalTime VISIT_TIME = LocalTime.of(15, 30);
    private static final BigDecimal AMOUNT = new BigDecimal("120.00");

    @Test
    void testCreateVisitHistoryEntry_shouldReturnSavedEntity() {
        EventClientEntity eventClient = new EventClientEntity();
        eventClient.setEventUserId(new EventClientId(CLIENT_ID, 2L));

        VisitHistoryEntity mappedEntity = VisitHistoryEntity.builder()
                .serviceName(SERVICE_NAME)
                .date(LocalDate.of(2024, 4, 29))
                .time(LocalTime.of(10, 0))
                .serviceColor(SERVICE_COLOR)
                .amountPaid(new BigDecimal("150.00"))
                .paymentMethodForStory(PaymentMethodForStory.CASH)
                .build();

        VisitHistoryEntity savedEntity = VisitHistoryEntity.builder()
                .id(100L)
                .serviceName(SERVICE_NAME)
                .date(LocalDate.of(2024, 4, 29))
                .time(LocalTime.of(10, 0))
                .serviceColor(SERVICE_COLOR)
                .amountPaid(new BigDecimal("150.00"))
                .paymentMethodForStory(PaymentMethodForStory.CASH)
                .build();

        when(visitHistoryMapper.toVisitHistoryEntity(eventClient)).thenReturn(mappedEntity);
        when(visitHistoryRepository.save(mappedEntity)).thenReturn(savedEntity);

        VisitHistoryEntity result = visitHistoryService.createVisitHistoryEntry(eventClient);

        assertNotNull(result);
        assertEquals(100L, result.getId());
        assertEquals(SERVICE_NAME, result.getServiceName());
        assertEquals(PaymentMethodForStory.CASH, result.getPaymentMethodForStory());

        verify(visitHistoryMapper).toVisitHistoryEntity(eventClient);
        verify(visitHistoryRepository).save(mappedEntity);
    }

    @Test
    void testGetVisitHistoryByClientId_shouldReturnDtoList() {
        VisitHistoryEntity mockEntity = VisitHistoryEntity.builder()
                .id(1L)
                .serviceColor(SERVICE_COLOR)
                .serviceName(SERVICE_NAME)
                .trainerFullName(TRAINER_NAME)
                .date(VISIT_DATE)
                .time(VISIT_TIME)
                .roomName(ROOM_NAME)
                .paymentMethodForStory(PaymentMethodForStory.CASH)
                .amountPaid(AMOUNT)
                .build();

        VisitHistoryDTO mockDto = new VisitHistoryDTO(
                1L,
                CLIENT_ID,
                SERVICE_COLOR,
                SERVICE_NAME,
                TRAINER_NAME,
                VISIT_DATE,
                VISIT_TIME,
                ROOM_NAME,
                PaymentMethodForStory.CASH,
                AMOUNT
        );

        when(visitHistoryRepository.findAllByClientId(CLIENT_ID)).thenReturn(Collections.singletonList(mockEntity));
        when(visitHistoryMapper.toDtoList(anyList())).thenReturn(Collections.singletonList(mockDto));

        List<VisitHistoryDTO> result = visitHistoryService.getVisitHistoryByClientId(CLIENT_ID);

        assertNotNull(result);
        assertEquals(1, result.size());

        VisitHistoryDTO dto = result.getFirst();
        assertAll(
                () -> assertEquals(1L, dto.id()),
                () -> assertEquals(CLIENT_ID, dto.clientId()),
                () -> assertEquals(SERVICE_NAME, dto.serviceName()),
                () -> assertEquals(TRAINER_NAME, dto.trainerFullName()),
                () -> assertEquals(VISIT_DATE, dto.date()),
                () -> assertEquals(VISIT_TIME, dto.time()),
                () -> assertEquals(ROOM_NAME, dto.roomName()),
                () -> assertEquals(PaymentMethodForStory.CASH, dto.paymentMethodForStory()),
                () -> assertEquals(AMOUNT, dto.amountPaid())
        );

        verify(visitHistoryRepository).findAllByClientId(CLIENT_ID);
        verify(visitHistoryMapper).toDtoList(Collections.singletonList(mockEntity));
    }
}