package ua.shpp.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.shpp.dto.OrganizationModerationDTO;
import ua.shpp.entity.Organization;
import ua.shpp.entity.OrganizationAccessEntity;
import ua.shpp.exception.EntityNotFoundException;
import ua.shpp.mapper.OrganizationModerationMapper;
import ua.shpp.repository.OrganizationAccessRepository;
import ua.shpp.repository.OrganizationRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrganizationModerationServiceTest {

    @Mock
    private OrganizationRepository organizationRepository;

    @Mock
    private OrganizationAccessRepository accessRepository;

    @Mock
    private OrganizationModerationMapper organizationModerationMapper;

    @InjectMocks
    private OrganizationModerationService organizationModerationService;

    // --- Helper Methods for Mocks ---
    private Organization createMockOrganization(Long id, String name) {
        Organization org = mock(Organization.class);
        lenient().when(org.getId()).thenReturn(id);
        lenient().when(org.getName()).thenReturn(name);
        return org;
    }

    private OrganizationAccessEntity createMockOrganizationAccessEntity(Long id, boolean accessAllowed) {
        OrganizationAccessEntity access = mock(OrganizationAccessEntity.class);
        lenient().when(access.getOrganizationId()).thenReturn(id);
        lenient().when(access.isAccessAllowed()).thenReturn(accessAllowed);
        lenient().doAnswer(invocation -> {
            boolean value = invocation.getArgument(0);
            lenient().when(access.isAccessAllowed()).thenReturn(value);
            return null;
        }).when(access).setAccessAllowed(anyBoolean());
        return access;
    }

    private OrganizationModerationDTO createMockOrganizationModerationDTO(Long id, String name, boolean access) {
        OrganizationModerationDTO dto = mock(OrganizationModerationDTO.class);
        lenient().when(dto.id()).thenReturn(id);
        lenient().when(dto.name()).thenReturn(name);
        lenient().when(dto.accessAllowed()).thenReturn(access);
        return dto;
    }

    @Test
    void getAllForModeration_returnsEmptyListWhenNoOrganizations() {
        // Arrange
        when(organizationRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<OrganizationModerationDTO> result = organizationModerationService.getAllForModeration();

        // Assert
        assertTrue(result.isEmpty());
        verify(organizationRepository).findAll();
        verifyNoInteractions(accessRepository);
        verifyNoInteractions(organizationModerationMapper);
    }

    @Test
    void getAllForModeration_returnsDTOsWithCorrectAccess() {
        // Arrange
        Organization org1 = createMockOrganization(1L, "Org 1");
        Organization org2 = createMockOrganization(2L, "Org 2");
        Organization org3 = createMockOrganization(3L, "Org 3");

        List<Organization> organizations = Arrays.asList(org1, org2, org3);

        OrganizationAccessEntity access1 = createMockOrganizationAccessEntity(1L, true);
        OrganizationAccessEntity access3 = createMockOrganizationAccessEntity(3L, false);

        OrganizationModerationDTO dto1 = createMockOrganizationModerationDTO(1L, "Org 1", true);
        OrganizationModerationDTO dto2 = createMockOrganizationModerationDTO(2L, "Org 2", false);
        OrganizationModerationDTO dto3 = createMockOrganizationModerationDTO(3L, "Org 3", false);


        when(organizationRepository.findAll()).thenReturn(organizations);
        when(accessRepository.findById(1L)).thenReturn(Optional.of(access1));
        when(accessRepository.findById(2L)).thenReturn(Optional.empty());
        when(accessRepository.findById(3L)).thenReturn(Optional.of(access3));

        when(organizationModerationMapper.toDto(org1, true)).thenReturn(dto1);
        when(organizationModerationMapper.toDto(org2, false)).thenReturn(dto2);
        when(organizationModerationMapper.toDto(org3, false)).thenReturn(dto3);

        // Act
        List<OrganizationModerationDTO> result = organizationModerationService.getAllForModeration();

        // Assert
        assertEquals(3, result.size());
        assertTrue(result.contains(dto1));
        assertTrue(result.contains(dto2));
        assertTrue(result.contains(dto3));

        verify(organizationRepository).findAll();
        verify(accessRepository).findById(1L);
        verify(accessRepository).findById(2L);
        verify(accessRepository).findById(3L);
        verify(organizationModerationMapper).toDto(org1, true);
        verify(organizationModerationMapper).toDto(org2, false);
        verify(organizationModerationMapper).toDto(org3, false);

        verifyNoMoreInteractions(organizationRepository, accessRepository, organizationModerationMapper);
    }

    @Test
    void deleteOrganization_callsDeleteOnBothRepositories() {
        // Arrange
        Long orgId = 123L;
        Organization mockOrganization = createMockOrganization(orgId, "Test Org");
        when(organizationRepository.findById(orgId)).thenReturn(Optional.of(mockOrganization));

        // Act
        organizationModerationService.deleteOrganization(orgId);

        // Assert
        verify(organizationRepository).findById(orgId);
        verify(accessRepository).deleteById(orgId);
        verify(organizationRepository).deleteById(orgId);
        verifyNoMoreInteractions(organizationRepository, accessRepository);
        verifyNoInteractions(organizationModerationMapper);
    }

    @Test
    void toggleAccess_throwsEntityNotFoundExceptionWhenOrganizationNotFound() {
        // Arrange
        Long orgId = 456L;
        when(organizationRepository.findById(orgId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> organizationModerationService.toggleAccess(orgId));

        assertEquals("Organization not found", exception.getMessage());
        verify(organizationRepository).findById(orgId);
        verifyNoInteractions(accessRepository);
        verifyNoInteractions(organizationModerationMapper);
        verifyNoMoreInteractions(organizationRepository);
    }

    @Test
    void toggleAccess_togglesAccessFromTrueToFalseAndSaves() {
        // Arrange
        Long orgId = 789L;
        Organization org = createMockOrganization(orgId, "Toggle Org 1");
        OrganizationAccessEntity access = createMockOrganizationAccessEntity(orgId, true);

        when(organizationRepository.findById(orgId)).thenReturn(Optional.of(org));
        when(accessRepository.findById(orgId)).thenReturn(Optional.of(access));
        when(accessRepository.save(any(OrganizationAccessEntity.class))).thenReturn(access);

        // Act
        organizationModerationService.toggleAccess(orgId);

        // Assert
        verify(organizationRepository).findById(orgId);
        verify(accessRepository).findById(orgId);
        verify(access).setAccessAllowed(false);
        verify(accessRepository).save(access);
        verifyNoMoreInteractions(organizationRepository, accessRepository);
        verifyNoInteractions(organizationModerationMapper);
    }

    @Test
    void toggleAccess_togglesAccessFromFalseToTrueAndSaves() {
        // Arrange
        Long orgId = 987L;
        Organization org = createMockOrganization(orgId, "Toggle Org 2");
        OrganizationAccessEntity access = createMockOrganizationAccessEntity(orgId, false);

        when(organizationRepository.findById(orgId)).thenReturn(Optional.of(org));
        when(accessRepository.findById(orgId)).thenReturn(Optional.of(access));
        when(accessRepository.save(any(OrganizationAccessEntity.class))).thenReturn(access);

        // Act
        organizationModerationService.toggleAccess(orgId);

        // Assert
        verify(organizationRepository).findById(orgId);
        verify(accessRepository).findById(orgId);
        verify(access).setAccessAllowed(true);
        verify(accessRepository).save(access);
        verifyNoMoreInteractions(organizationRepository, accessRepository);
        verifyNoInteractions(organizationModerationMapper);
    }

    @Test
    void toggleAccess_createsNewAccessEntryTogglesToTrueAndSavesWhenNotFound() {
        // Arrange
        Long orgId = 654L;
        Organization org = createMockOrganization(orgId, "New Access Org");

        when(organizationRepository.findById(orgId)).thenReturn(Optional.of(org));
        when(accessRepository.findById(orgId)).thenReturn(Optional.empty());

        ArgumentCaptor<OrganizationAccessEntity> accessCaptor = ArgumentCaptor.forClass(OrganizationAccessEntity.class);

        when(accessRepository.save(accessCaptor.capture())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        organizationModerationService.toggleAccess(orgId);

        // Assert
        verify(organizationRepository).findById(orgId);
        verify(accessRepository).findById(orgId);
        verify(accessRepository).save(any(OrganizationAccessEntity.class));

        OrganizationAccessEntity capturedAccess = accessCaptor.getValue();

        assertNotNull(capturedAccess);
        assertTrue(capturedAccess.isAccessAllowed(), "Access should be true after toggle");
        assertEquals(org, capturedAccess.getOrganization(),
                "Captured access should be linked to the organization");

        verifyNoMoreInteractions(organizationRepository, accessRepository);
        verifyNoInteractions(organizationModerationMapper);
    }
}