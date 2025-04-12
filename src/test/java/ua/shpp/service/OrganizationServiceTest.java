package ua.shpp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.shpp.dto.OrganizationRequestDTO;
import ua.shpp.entity.Organization;
import ua.shpp.exception.OrganizationAlreadyExists;
import ua.shpp.exception.OrganizationNotFound;
import ua.shpp.repository.OrganizationRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrganizationServiceTest {

    private OrganizationRepository repository;
    private OrganizationService service;

    @BeforeEach
    void setUp() {
        repository = mock(OrganizationRepository.class);
        service = new OrganizationService(repository);
    }

    @Test
    void update_sameName_shouldPass() {
        Organization org = Organization.builder()
                .id(1L)
                .name("Apple")
                .build();

        OrganizationRequestDTO dto = new OrganizationRequestDTO("Apple");

        when(repository.findById(1L)).thenReturn(Optional.of(org));
        when(repository.existsByName("Apple")).thenReturn(true); // це норм, бо це "наше" ім'я

        assertDoesNotThrow(() -> service.update(1L, dto));
        verify(repository).save(org);
    }

    @Test
    void update_uniqueNewName_shouldPass() {
        Organization org = Organization.builder()
                .id(1L)
                .name("Apple")
                .build();

        OrganizationRequestDTO dto = new OrganizationRequestDTO("Google");

        when(repository.findById(1L)).thenReturn(Optional.of(org));
        when(repository.existsByName("Google")).thenReturn(false);

        assertDoesNotThrow(() -> service.update(1L, dto));
        verify(repository).save(org);
        assertEquals("Google", org.getName());
    }

    @Test
    void update_toExistingOtherName_shouldThrow() {
        Organization org = Organization.builder()
                .id(1L)
                .name("Apple")
                .build();

        OrganizationRequestDTO dto = new OrganizationRequestDTO("Microsoft");

        when(repository.findById(1L)).thenReturn(Optional.of(org));
        when(repository.existsByName("Microsoft")).thenReturn(true);

        assertThrows(OrganizationAlreadyExists.class, () -> service.update(1L, dto));
        verify(repository, never()).save(any());
    }

    @Test
    void update_nonExistentOrganization_shouldThrow() {
        OrganizationRequestDTO dto = new OrganizationRequestDTO("Anything");

        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(OrganizationNotFound.class, () -> service.update(999L, dto));
        verify(repository, never()).save(any());
    }
}
