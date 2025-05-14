package ua.shpp.service.history;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.shpp.dto.SubscriptionHistoryDTO;
import ua.shpp.entity.ClientEntity;
import ua.shpp.entity.EventTypeEntity;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscriptionHistoryServiceTest {

    @Mock
    private SubscriptionHistoryRepository subscriptionHistoryRepository;

    @Mock
    private SubscriptionHistoryMapper subscriptionHistoryMapper;

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private SubscriptionHistoryService subscriptionHistoryService;

    @Mock
    private SubscriptionDealRepository subscriptionDealRepository;

    @Mock
    private SubscriptionDealEntity subscriptionInfo;

    private ClientEntity createMockClient(Long id) {
        ClientEntity client = mock(ClientEntity.class);
        lenient().when(client.getId()).thenReturn(id);
        return client;
    }

    private EventTypeEntity createMockEventType(String name) {
        EventTypeEntity eventType = mock(EventTypeEntity.class);
        lenient().when(eventType.getName()).thenReturn(name);
        return eventType;
    }

    private SubscriptionOfferEntity createMockSubscriptionService(String name, EventTypeEntity eventType,
                                                                  Integer totalVisits) {
        SubscriptionOfferEntity service = mock(SubscriptionOfferEntity.class);
        lenient().when(service.getName()).thenReturn(name);
        lenient().when(service.getEventType()).thenReturn(eventType);
        lenient().when(service.getVisits()).thenReturn(totalVisits);
        return service;
    }

    private SubscriptionDealEntity createMockSubscriptionInfo(Long id, ClientEntity client,
                                                              SubscriptionOfferEntity service,
                                                              Integer visits, LocalDate expirationDate) {
        SubscriptionDealEntity info = mock(SubscriptionDealEntity.class);
        lenient().when(info.getId()).thenReturn(id);
        lenient().when(info.getClient()).thenReturn(client);
        lenient().when(info.getSubscriptionService()).thenReturn(service);
        lenient().when(info.getVisits()).thenReturn(visits);
        lenient().when(info.getExpirationDate()).thenReturn(expirationDate);
        return info;
    }

    private SubscriptionHistoryEntity createMockSubscriptionHistory(ClientEntity client, String name,
                                                                    String eventType, Integer totalVisits,
                                                                    Integer visitsLeft) {
        SubscriptionHistoryEntity history = mock(SubscriptionHistoryEntity.class);
        lenient().when(history.getId()).thenReturn(1L);
        lenient().when(history.getClient()).thenReturn(client);
        lenient().when(history.getName()).thenReturn(name);
        lenient().when(history.getEventType()).thenReturn(eventType);
        lenient().when(history.getTotalVisits()).thenReturn(totalVisits);
        lenient().when(history.getVisitsLeft()).thenReturn(visitsLeft);
        lenient().when(history.getIsValid()).thenReturn(true);
        return history;
    }

    @Test
    void createSubscriptionHistory_success() {
        // Arrange
        ClientEntity mockClient = createMockClient(1L);
        EventTypeEntity mockEventType = createMockEventType("Group");
        SubscriptionOfferEntity mockService = createMockSubscriptionService("Yoga Pass",
                mockEventType, 10);
        SubscriptionDealEntity mockSubscriptionInfo = createMockSubscriptionInfo(100L, mockClient,
                mockService, 10, LocalDate.now().plusDays(30));
        SubscriptionHistoryEntity mockHistoryEntity = createMockSubscriptionHistory(mockClient,
                "Yoga Pass", "Group", 10, 10);

        when(subscriptionHistoryMapper.toHistory(mockSubscriptionInfo, mockService)).thenReturn(mockHistoryEntity);
        when(subscriptionHistoryRepository.save(any(SubscriptionHistoryEntity.class))).thenReturn(mockHistoryEntity);

        // Act
        assertDoesNotThrow(() -> subscriptionHistoryService.createSubscriptionHistory(mockSubscriptionInfo));

        // Assert
        verify(mockSubscriptionInfo, atLeastOnce()).getSubscriptionService();
        verify(subscriptionHistoryMapper).toHistory(mockSubscriptionInfo, mockService);
        verify(subscriptionHistoryRepository).save(mockHistoryEntity);
        verify(mockSubscriptionInfo, atLeastOnce()).getId();
    }


    @Test
    void createSubscriptionHistory_serviceIsNull_throwsMissingSubscriptionServiceException() {
        // Arrange
        SubscriptionDealEntity mockSubscriptionInfo = mock(SubscriptionDealEntity.class);
        when(mockSubscriptionInfo.getSubscriptionService()).thenReturn(null);
        when(mockSubscriptionInfo.getId()).thenReturn(100L);

        // Act & Assert
        MissingSubscriptionServiceException thrown = assertThrows(MissingSubscriptionServiceException.class,
                () -> subscriptionHistoryService.createSubscriptionHistory(mockSubscriptionInfo));

        assertEquals("SubscriptionService is null for ID: 100", thrown.getMessage());

        // Verify
        verify(mockSubscriptionInfo, atLeastOnce()).getSubscriptionService();
        verify(mockSubscriptionInfo, atLeastOnce()).getId();
        verifyNoInteractions(subscriptionHistoryMapper);
        verifyNoInteractions(subscriptionHistoryRepository);
    }

    @Test
    void createSubscriptionHistory_repositorySaveThrowsException_throwsCustomException() {
        // Arrange
        ClientEntity mockClient = createMockClient(1L);
        EventTypeEntity mockEventType = createMockEventType("Group");
        SubscriptionOfferEntity mockService = createMockSubscriptionService("Yoga Pass",
                mockEventType, 10);
        SubscriptionDealEntity mockSubscriptionInfo = createMockSubscriptionInfo(100L, mockClient,
                mockService, 10, LocalDate.now().plusDays(30));
        SubscriptionHistoryEntity mockHistoryEntity = createMockSubscriptionHistory(mockClient,
                "Yoga Pass", "Group", 10, 10);

        when(subscriptionHistoryMapper.toHistory(mockSubscriptionInfo, mockService)).thenReturn(mockHistoryEntity);
        when(subscriptionHistoryRepository.save(any(SubscriptionHistoryEntity.class)))
                .thenThrow(new RuntimeException("DB error"));

        // Act & Assert
        SubscriptionHistoryCreationException thrown = assertThrows(SubscriptionHistoryCreationException.class,
                () -> subscriptionHistoryService.createSubscriptionHistory(mockSubscriptionInfo));

        assertTrue(thrown.getMessage().contains("Failed to save history for ID: 100"));
        assertNotNull(thrown.getCause());
        assertEquals("DB error", thrown.getCause().getMessage());

        // Verify
        verify(mockSubscriptionInfo, atLeastOnce()).getSubscriptionService();
        verify(subscriptionHistoryMapper).toHistory(mockSubscriptionInfo, mockService);
        verify(subscriptionHistoryRepository).save(mockHistoryEntity);
        verify(mockSubscriptionInfo, atLeastOnce()).getId();
    }


    @Test
    void getSubscriptionHistoryByClientAndValidity_clientFoundButNoHistoryFound_returnsEmptyList() {
        // Arrange
        Long clientId = 1L;
        Boolean isValid = true;
        ClientEntity mockClient = createMockClient(clientId);

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(mockClient));
        when(subscriptionHistoryRepository.findByClientIdAndIsValid(clientId, isValid))
                .thenReturn(Collections.emptyList());

        // Act
        List<SubscriptionHistoryDTO> result =
                subscriptionHistoryService.getSubscriptionHistoryByClientAndValidity(clientId, isValid);

        // Assert
        assertTrue(result.isEmpty());
        verify(clientRepository).findById(clientId);
        verify(subscriptionHistoryRepository).findByClientIdAndIsValid(clientId, isValid);
        verifyNoInteractions(subscriptionHistoryMapper);
    }

    @Test
    void getAllSubscriptionHistoryByClient_clientFoundButNoHistoryFound_returnsEmptyList() {
        // Arrange
        Long clientId = 1L;
        ClientEntity mockClient = createMockClient(clientId);

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(mockClient));
        when(subscriptionHistoryRepository.findByClientId(clientId)).thenReturn(Collections.emptyList());

        // Act
        List<SubscriptionHistoryDTO> result =
                subscriptionHistoryService.getAllSubscriptionHistoryByClient(clientId);

        // Assert
        assertTrue(result.isEmpty());
        verify(clientRepository).findById(clientId);
        verify(subscriptionHistoryRepository).findByClientId(clientId);
        verifyNoInteractions(subscriptionHistoryMapper);
    }

    @Test
    void updateHistoryVisitsRemaining_success_validAfterUpdate() {
        // Arrange
        Long subscriptionInfoId = 100L;
        Long clientId = 1L;
        ClientEntity mockClient = createMockClient(clientId);
        EventTypeEntity mockEventType = createMockEventType("Group");
        SubscriptionOfferEntity mockService = createMockSubscriptionService("Yoga Pass",
                mockEventType, 10);
        SubscriptionDealEntity mockSubscriptionInfo = createMockSubscriptionInfo(subscriptionInfoId,
                mockClient, mockService, 5, LocalDate.now().plusDays(10));

        SubscriptionHistoryEntity mockHistoryToUpdate = createMockSubscriptionHistory(mockClient,
                "Yoga Pass", "Group", 10, 6);

        when(subscriptionHistoryRepository.findByClientIdAndNameAndEventTypeAndIsValid(
                clientId, "Yoga Pass", "Group", true))
                .thenReturn(Optional.of(mockHistoryToUpdate));
        when(subscriptionHistoryRepository.save(any(SubscriptionHistoryEntity.class))).thenReturn(mockHistoryToUpdate);

        // Act
        assertDoesNotThrow(() -> subscriptionHistoryService.updateHistoryVisitsRemaining(mockSubscriptionInfo));

        // Assert
        verify(mockSubscriptionInfo).getClient();
        verify(mockSubscriptionInfo).getSubscriptionService();
        verify(mockClient).getId();
        verify(mockService).getName();
        verify(mockService).getEventType();
        verify(mockEventType).getName();

        verify(subscriptionHistoryRepository).findByClientIdAndNameAndEventTypeAndIsValid(
                clientId, "Yoga Pass", "Group", true);

        verify(mockHistoryToUpdate).setVisitsLeft(5);
        verify(mockHistoryToUpdate).setIsValid(true);

        verify(subscriptionHistoryRepository).save(mockHistoryToUpdate);
        verifyNoMoreInteractions(subscriptionHistoryRepository);
        verifyNoInteractions(subscriptionHistoryMapper);
    }

    @Test
    void updateHistoryVisitsRemaining_success_invalidAfterUpdate_visitsZero() {
        // Arrange
        Long subscriptionInfoId = 100L;
        Long clientId = 1L;
        ClientEntity mockClient = createMockClient(clientId);
        EventTypeEntity mockEventType = createMockEventType("Individual");
        SubscriptionOfferEntity mockService = createMockSubscriptionService("Training",
                mockEventType, 5);
        SubscriptionDealEntity mockSubscriptionInfo = createMockSubscriptionInfo(subscriptionInfoId,
                mockClient, mockService, 0, LocalDate.now().plusDays(10));

        SubscriptionHistoryEntity mockHistoryToUpdate = createMockSubscriptionHistory(mockClient,
                "Training", "Individual", 5, 1);

        when(subscriptionHistoryRepository.findByClientIdAndNameAndEventTypeAndIsValid(
                clientId, "Training", "Individual", true))
                .thenReturn(Optional.of(mockHistoryToUpdate));
        when(subscriptionHistoryRepository.save(any(SubscriptionHistoryEntity.class))).thenReturn(mockHistoryToUpdate);

        // Act
        assertDoesNotThrow(() -> subscriptionHistoryService.updateHistoryVisitsRemaining(mockSubscriptionInfo));

        // Assert
        verify(subscriptionHistoryRepository).findByClientIdAndNameAndEventTypeAndIsValid(
                clientId, "Training", "Individual", true);

        verify(mockHistoryToUpdate).setVisitsLeft(0);
        verify(mockHistoryToUpdate).setIsValid(false);

        verify(subscriptionHistoryRepository).save(mockHistoryToUpdate);
        verifyNoMoreInteractions(subscriptionHistoryRepository);
    }

    @Test
    void updateHistoryVisitsRemaining_success_invalidAfterUpdate_dateExpired() {
        // Arrange
        Long subscriptionInfoId = 100L;
        Long clientId = 1L;
        ClientEntity mockClient = createMockClient(clientId);
        EventTypeEntity mockEventType = createMockEventType("Group");
        SubscriptionOfferEntity mockService = createMockSubscriptionService("Crossfit",
                mockEventType, 8);
        SubscriptionDealEntity mockSubscriptionInfo = createMockSubscriptionInfo(subscriptionInfoId,
                mockClient, mockService, 3, LocalDate.now().minusDays(1));

        SubscriptionHistoryEntity mockHistoryToUpdate = createMockSubscriptionHistory(mockClient,
                "Crossfit", "Group", 8, 4);

        when(subscriptionHistoryRepository.findByClientIdAndNameAndEventTypeAndIsValid(
                clientId, "Crossfit", "Group", true))
                .thenReturn(Optional.of(mockHistoryToUpdate));
        when(subscriptionHistoryRepository.save(any(SubscriptionHistoryEntity.class))).thenReturn(mockHistoryToUpdate);

        // Act
        assertDoesNotThrow(() -> subscriptionHistoryService.updateHistoryVisitsRemaining(mockSubscriptionInfo));

        // Assert
        verify(subscriptionHistoryRepository).findByClientIdAndNameAndEventTypeAndIsValid(
                clientId, "Crossfit", "Group", true);

        verify(mockHistoryToUpdate).setVisitsLeft(3);
        verify(mockHistoryToUpdate).setIsValid(false);

        verify(subscriptionHistoryRepository).save(mockHistoryToUpdate);
        verifyNoMoreInteractions(subscriptionHistoryRepository);
    }

    @Test
    void updateHistoryVisitsRemaining_historyNotFound_throwsIllegalStateException() {
        // Arrange
        Long subscriptionInfoId = 100L;
        Long clientId = 1L;
        ClientEntity mockClient = createMockClient(clientId);
        EventTypeEntity mockEventType = createMockEventType("Group");
        SubscriptionOfferEntity mockService = createMockSubscriptionService("Yoga Pass",
                mockEventType, 10);
        SubscriptionDealEntity mockSubscriptionInfo = createMockSubscriptionInfo(subscriptionInfoId,
                mockClient, mockService, 5, LocalDate.now().plusDays(10));

        when(subscriptionHistoryRepository.findByClientIdAndNameAndEventTypeAndIsValid(
                clientId, "Yoga Pass", "Group", true))
                .thenReturn(Optional.empty());

        // Act & Assert
        IllegalStateException thrown = assertThrows(IllegalStateException.class,
                () -> subscriptionHistoryService.updateHistoryVisitsRemaining(mockSubscriptionInfo));
        assertTrue(thrown.getMessage().contains("No history record found to update for SubscriptionInfo ID: "
                + subscriptionInfoId));

        // Assert
        verify(subscriptionHistoryRepository).findByClientIdAndNameAndEventTypeAndIsValid(
                clientId, "Yoga Pass", "Group", true);
        verifyNoMoreInteractions(subscriptionHistoryRepository);
        verifyNoInteractions(subscriptionHistoryMapper);
    }

    @Test
    void getSubscriptionHistoryByClientAndValidity_clientNotFound_throwsClientNotFoundException() {
        // Arrange
        Long clientId = 1L;
        Boolean isValid = true;
        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ClientNotFoundException.class,
                () -> subscriptionHistoryService.getSubscriptionHistoryByClientAndValidity(clientId, isValid));

        // Assert
        verify(clientRepository).findById(clientId);
        verifyNoInteractions(subscriptionHistoryRepository);
        verifyNoInteractions(subscriptionHistoryMapper);
    }

    @Test
    void getAllSubscriptionHistoryByClient_clientNotFound_throwsClientNotFoundException() {
        // Arrange
        Long clientId = 1L;
        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ClientNotFoundException.class,
                () -> subscriptionHistoryService.getAllSubscriptionHistoryByClient(clientId));

        // Assert
        verify(clientRepository).findById(clientId);
        verifyNoInteractions(subscriptionHistoryRepository);
        verifyNoInteractions(subscriptionHistoryMapper);
    }

    @Test
    void validateActiveSubscriptions_subscriptionInvalidatedSuccessfully() {
        // Arrange
        ClientEntity client = createMockClient(1L);
        EventTypeEntity eventType = createMockEventType("Group");
        SubscriptionOfferEntity service = createMockSubscriptionService("Yoga Pass", eventType, 10);

        subscriptionInfo = createMockSubscriptionInfo(
                100L, client, service, 0, LocalDate.now().minusDays(1));

        SubscriptionHistoryEntity history = createMockSubscriptionHistory(
                client, "Yoga Pass", "Group", 10, 1);

        when(subscriptionHistoryRepository.findAll()).thenReturn(List.of(history));
        when(subscriptionDealRepository.findByClientIdAndSubscriptionService_NameAndSubscriptionService_EventType_Name(
                1L, "Yoga Pass", "Group")).thenReturn(Optional.of(subscriptionInfo));

        // Act
        subscriptionHistoryService.validateActiveSubscriptions();

        // Assert
        verify(history).setIsValid(false);
        verify(subscriptionHistoryRepository).save(history);
    }

    @Test
    void validateActiveSubscriptions_noMatchingInfo_logsWarning() {
        // Arrange
        Long clientId = 1L;
        String subscriptionName = "Nonexistent";
        String eventType = "Group";

        ClientEntity mockClient = createMockClient(clientId);
        SubscriptionHistoryEntity mockHistory = mock(SubscriptionHistoryEntity.class);
        when(mockHistory.getClient()).thenReturn(mockClient);
        when(mockHistory.getName()).thenReturn(subscriptionName);
        when(mockHistory.getEventType()).thenReturn(eventType);
        when(mockHistory.getId()).thenReturn(99L);

        when(subscriptionHistoryRepository.findAll()).thenReturn(List.of(mockHistory));
        when(subscriptionDealRepository.findByClientIdAndSubscriptionService_NameAndSubscriptionService_EventType_Name(
                clientId, subscriptionName, eventType)).thenReturn(Optional.empty());

        // Act
        subscriptionHistoryService.validateActiveSubscriptions();

        // Assert
        verify(mockHistory, never()).setIsValid(false);
        verify(subscriptionHistoryRepository, never()).save(any());
    }

    @Test
    void validateActiveSubscriptions_invalidatedDueToExpirationDate() {
        // Arrange
        ClientEntity client = createMockClient(1L);
        EventTypeEntity eventType = createMockEventType("Group");
        SubscriptionOfferEntity service = createMockSubscriptionService("Yoga Pass", eventType, 10);

        // Візитів > 0, але дата вже в минулому
        subscriptionInfo = createMockSubscriptionInfo(
                100L, client, service, 5, LocalDate.now().minusDays(1));

        SubscriptionHistoryEntity history = createMockSubscriptionHistory(
                client, "Yoga Pass", "Group", 10, 5);
        lenient().when(history.getIsValid()).thenReturn(true);

        when(subscriptionHistoryRepository.findAll()).thenReturn(List.of(history));
        when(subscriptionDealRepository.findByClientIdAndSubscriptionService_NameAndSubscriptionService_EventType_Name(
                1L, "Yoga Pass", "Group")).thenReturn(Optional.of(subscriptionInfo));

        // Act
        subscriptionHistoryService.validateActiveSubscriptions();

        // Assert
        verify(history).setIsValid(false);
        verify(subscriptionHistoryRepository).save(history);
    }

    @Test
    void validateActiveSubscriptions_validWhenExpiresToday() {
        // Arrange
        ClientEntity client = createMockClient(1L);
        EventTypeEntity eventType = createMockEventType("Group");
        SubscriptionOfferEntity service = createMockSubscriptionService("Yoga Pass", eventType, 10);

        // Візитів > 0, дата закінчення = сьогодні
        subscriptionInfo = createMockSubscriptionInfo(
                100L, client, service, 3, LocalDate.now());

        SubscriptionHistoryEntity history = createMockSubscriptionHistory(
                client, "Yoga Pass", "Group", 10, 3);
        lenient().when(history.getIsValid()).thenReturn(true);

        when(subscriptionHistoryRepository.findAll()).thenReturn(List.of(history));
        when(subscriptionDealRepository.findByClientIdAndSubscriptionService_NameAndSubscriptionService_EventType_Name(
                1L, "Yoga Pass", "Group")).thenReturn(Optional.of(subscriptionInfo));

        // Act
        subscriptionHistoryService.validateActiveSubscriptions();

        // Assert
        // Нічого не змінюється, бо підписка все ще дійсна
        verify(history, never()).setIsValid(false);
        verify(subscriptionHistoryRepository, never()).save(any());
    }

    @Test
    void validateActiveSubscriptions_invalidWhenNoVisitsLeftButNotExpired() {
        // Arrange
        ClientEntity client = createMockClient(1L);
        EventTypeEntity eventType = createMockEventType("Group");
        SubscriptionOfferEntity service = createMockSubscriptionService("Pilates", eventType, 5);

        // Візити = 0, але ще не прострочена
        subscriptionInfo = createMockSubscriptionInfo(
                200L, client, service, 0, LocalDate.now().plusDays(5));

        SubscriptionHistoryEntity history = createMockSubscriptionHistory(
                client, "Pilates", "Group", 5, 1);
        lenient().when(history.getIsValid()).thenReturn(true);

        when(subscriptionHistoryRepository.findAll()).thenReturn(List.of(history));
        when(subscriptionDealRepository.findByClientIdAndSubscriptionService_NameAndSubscriptionService_EventType_Name(
                1L, "Pilates", "Group")).thenReturn(Optional.of(subscriptionInfo));

        // Act
        subscriptionHistoryService.validateActiveSubscriptions();

        // Assert
        // Має бути інвалідована
        verify(history).setIsValid(false);
        verify(subscriptionHistoryRepository).save(history);
    }
}