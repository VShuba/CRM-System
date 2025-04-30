package ua.shpp.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.shpp.entity.EventClientEntity;
import ua.shpp.entity.EventClientId;
import ua.shpp.entity.VisitHistoryEntity;
import ua.shpp.mapper.VisitHistoryMapper;
import ua.shpp.repository.VisitHistoryRepository;
import ua.shpp.model.PaymentMethodForStory;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

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

    @Test
    void testCreateVisitHistoryEntry_shouldSaveVisitHistory() {
        EventClientEntity mockEventClient = new EventClientEntity();
        EventClientId eventClientId = new EventClientId(1L, 2L);
        mockEventClient.setEventUserId(eventClientId);

        VisitHistoryEntity mappedEntity = VisitHistoryEntity.builder()
                .clientId(1L)
                .serviceName("Йога")
                .date(LocalDate.of(2024, 4, 29))
                .time(LocalTime.of(10, 0))
                .serviceColor("#FF0000")
                .amountPaid(new BigDecimal("150.00"))
                .paymentMethodForStory(PaymentMethodForStory.CASH)
                .build();

        VisitHistoryEntity savedEntity = VisitHistoryEntity.builder()
                .id(100L)
                .clientId(1L)
                .serviceName("Йога")
                .date(LocalDate.of(2024, 4, 29))
                .time(LocalTime.of(10, 0))
                .serviceColor("#FF0000")
                .amountPaid(new BigDecimal("150.00"))
                .paymentMethodForStory(PaymentMethodForStory.CASH)
                .build();

        // Мапер повертає готову ентіті
        when(visitHistoryMapper.toVisitHistoryEntity(mockEventClient)).thenReturn(mappedEntity);
        // Репозиторій зберігає і повертає ентіті з ID
        when(visitHistoryRepository.save(mappedEntity)).thenReturn(savedEntity);

        // Act
        VisitHistoryEntity result = visitHistoryService.createVisitHistoryEntry(mockEventClient);

        // Assert
        assertNotNull(result);
        assertEquals(100L, result.getId());
        assertEquals("Йога", result.getServiceName());
        assertEquals(PaymentMethodForStory.CASH, result.getPaymentMethodForStory());

        verify(visitHistoryMapper).toVisitHistoryEntity(mockEventClient);
        verify(visitHistoryRepository).save(mappedEntity);
    }
}