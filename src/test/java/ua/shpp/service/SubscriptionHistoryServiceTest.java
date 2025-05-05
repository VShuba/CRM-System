package ua.shpp.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.shpp.dto.SubscriptionHistoryDTO;
import ua.shpp.entity.ClientEntity;
import ua.shpp.entity.EventTypeEntity;
import ua.shpp.entity.SubscriptionHistoryEntity;
import ua.shpp.entity.SubscriptionServiceEntity;
import ua.shpp.entity.payment.SubscriptionInfoEntity;
import ua.shpp.exception.ClientNotFoundException;
import ua.shpp.mapper.SubscriptionHistoryMapper;
import ua.shpp.repository.ClientRepository;
import ua.shpp.repository.SubscriptionHistoryRepository;

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

    // Helper method to create a mock ClientEntity
    private ClientEntity createMockClient(Long id) {
        ClientEntity client = mock(ClientEntity.class, withSettings().lenient());
        when(client.getId()).thenReturn(id);
        return client;
    }

    // Helper method to create a mock EventTypeEntity
    private EventTypeEntity createMockEventType(String name) {
        EventTypeEntity eventType = mock(EventTypeEntity.class, withSettings().lenient());
        when(eventType.getName()).thenReturn(name);
        return eventType;
    }

    // Helper method to create a mock SubscriptionServiceEntity
    private SubscriptionServiceEntity createMockSubscriptionService(String name, EventTypeEntity eventType,
                                                                    Integer totalVisits) {
        SubscriptionServiceEntity service = mock(SubscriptionServiceEntity.class, withSettings().lenient());
        when(service.getName()).thenReturn(name);
        when(service.getEventType()).thenReturn(eventType);
        when(service.getVisits()).thenReturn(totalVisits);

        return service;
    }

    // Helper method to create a mock SubscriptionInfoEntity
    private SubscriptionInfoEntity createMockSubscriptionInfo(Long id, ClientEntity client,
                                                              SubscriptionServiceEntity service, Integer visits,
                                                              LocalDate expirationDate) {
        SubscriptionInfoEntity info = mock(SubscriptionInfoEntity.class, withSettings().lenient());
        when(info.getId()).thenReturn(id);
        when(info.getClient()).thenReturn(client);
        when(info.getSubscriptionService()).thenReturn(service);
        when(info.getVisits()).thenReturn(visits);
        when(info.getExpirationDate()).thenReturn(expirationDate);
        return info;
    }

    // Helper method to create a mock SubscriptionHistoryEntity
    private SubscriptionHistoryEntity createMockSubscriptionHistory(Long id, ClientEntity client, String name,
                                                                    String eventType, Integer totalVisits,
                                                                    Integer visitsLeft, Boolean isValid) {
        SubscriptionHistoryEntity history = mock(SubscriptionHistoryEntity.class, withSettings().lenient());
        when(history.getId()).thenReturn(id);
        when(history.getClient()).thenReturn(client);
        when(history.getName()).thenReturn(name);
        when(history.getEventType()).thenReturn(eventType);
        when(history.getTotalVisits()).thenReturn(totalVisits);
        when(history.getVisitsLeft()).thenReturn(visitsLeft);
        when(history.getIsValid()).thenReturn(isValid);

        doNothing().when(history).setVisitsLeft(anyInt());
        doNothing().when(history).setIsValid(anyBoolean());
        return history;
    }

    @Test
    void createSubscriptionHistory_success() {
        // Arrange
        ClientEntity mockClient = createMockClient(1L);
        EventTypeEntity mockEventType = createMockEventType("Group");
        SubscriptionServiceEntity mockService = createMockSubscriptionService("Yoga Pass",
                mockEventType, 10);
        SubscriptionInfoEntity mockSubscriptionInfo = createMockSubscriptionInfo(100L, mockClient,
                mockService, 10, LocalDate.now().plusDays(30));
        SubscriptionHistoryEntity mockHistoryEntity = createMockSubscriptionHistory(1L, mockClient,
                "Yoga Pass", "Group", 10, 10, true);

        when(subscriptionHistoryMapper.toHistory(mockSubscriptionInfo, mockService)).thenReturn(mockHistoryEntity);
        when(subscriptionHistoryRepository.save(any(SubscriptionHistoryEntity.class))).thenReturn(mockHistoryEntity);

        // Act
        assertDoesNotThrow(() -> subscriptionHistoryService.createSubscriptionHistory(mockSubscriptionInfo));

        // Assert
        verify(mockSubscriptionInfo).getSubscriptionService();
        verify(subscriptionHistoryMapper).toHistory(mockSubscriptionInfo, mockService);
        verify(subscriptionHistoryRepository).save(mockHistoryEntity);
        verify(mockSubscriptionInfo, atLeastOnce()).getId();
    }

    @Test
    void createSubscriptionHistory_serviceIsNull_throwsIllegalStateException() {
        // Arrange
        SubscriptionInfoEntity mockSubscriptionInfo = mock(SubscriptionInfoEntity.class);
        when(mockSubscriptionInfo.getSubscriptionService()).thenReturn(null);
        when(mockSubscriptionInfo.getId()).thenReturn(100L);

        // Act & Assert
        assertThrows(IllegalStateException.class, () ->
                subscriptionHistoryService.createSubscriptionHistory(mockSubscriptionInfo));

        // Assert
        verify(mockSubscriptionInfo).getSubscriptionService();
        verify(mockSubscriptionInfo, atLeastOnce()).getId();
        verifyNoInteractions(subscriptionHistoryMapper);
        verifyNoInteractions(subscriptionHistoryRepository);
    }

    @Test
    void createSubscriptionHistory_repositorySaveThrowsException_throwsRuntimeException() {
        // Arrange
        ClientEntity mockClient = createMockClient(1L);
        EventTypeEntity mockEventType = createMockEventType("Group");
        SubscriptionServiceEntity mockService = createMockSubscriptionService("Yoga Pass",
                mockEventType, 10);
        SubscriptionInfoEntity mockSubscriptionInfo = createMockSubscriptionInfo(100L, mockClient,
                mockService, 10, LocalDate.now().plusDays(30));
        SubscriptionHistoryEntity mockHistoryEntity = createMockSubscriptionHistory(1L, mockClient,
                "Yoga Pass", "Group", 10, 10, true);

        when(subscriptionHistoryMapper.toHistory(mockSubscriptionInfo, mockService)).thenReturn(mockHistoryEntity);
        when(subscriptionHistoryRepository.save(any(SubscriptionHistoryEntity.class)))
                .thenThrow(new RuntimeException("DB error"));

        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> subscriptionHistoryService.createSubscriptionHistory(mockSubscriptionInfo));
        assertTrue(thrown.getMessage().contains("Failed to save subscription history"));

        // Assert
        verify(mockSubscriptionInfo).getSubscriptionService();
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
        SubscriptionServiceEntity mockService = createMockSubscriptionService("Yoga Pass",
                mockEventType, 10);
        SubscriptionInfoEntity mockSubscriptionInfo = createMockSubscriptionInfo(subscriptionInfoId,
                mockClient, mockService, 5, LocalDate.now().plusDays(10));

        SubscriptionHistoryEntity mockHistoryToUpdate = createMockSubscriptionHistory(1L, mockClient,
                "Yoga Pass", "Group", 10, 6, true);

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
        SubscriptionServiceEntity mockService = createMockSubscriptionService("Training",
                mockEventType, 5);
        SubscriptionInfoEntity mockSubscriptionInfo = createMockSubscriptionInfo(subscriptionInfoId,
                mockClient, mockService, 0, LocalDate.now().plusDays(10));

        SubscriptionHistoryEntity mockHistoryToUpdate = createMockSubscriptionHistory(1L, mockClient,
                "Training", "Individual", 5, 1, true);

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
        SubscriptionServiceEntity mockService = createMockSubscriptionService("Crossfit",
                mockEventType, 8);
        SubscriptionInfoEntity mockSubscriptionInfo = createMockSubscriptionInfo(subscriptionInfoId,
                mockClient, mockService, 3, LocalDate.now().minusDays(1));

        SubscriptionHistoryEntity mockHistoryToUpdate = createMockSubscriptionHistory(1L, mockClient,
                "Crossfit", "Group", 8, 4, true);

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
        SubscriptionServiceEntity mockService = createMockSubscriptionService("Yoga Pass",
                mockEventType, 10);
        SubscriptionInfoEntity mockSubscriptionInfo = createMockSubscriptionInfo(subscriptionInfoId,
                mockClient, mockService, 5, LocalDate.now().plusDays(10));

        when(subscriptionHistoryRepository.findByClientIdAndNameAndEventTypeAndIsValid(
                clientId, "Yoga Pass", "Group", true))
                .thenReturn(Optional.empty());

        // Act & Assert
        IllegalStateException thrown = assertThrows(IllegalStateException.class,
                () -> subscriptionHistoryService.updateHistoryVisitsRemaining(mockSubscriptionInfo));
        assertTrue(thrown.getMessage().contains("Could not find relevant history record for SubscriptionInfo ID: "
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
}