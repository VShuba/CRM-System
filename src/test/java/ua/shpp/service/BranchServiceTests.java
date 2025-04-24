package ua.shpp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.shpp.dto.branch.BranchPatchRequestDTO;
import ua.shpp.dto.branch.BranchRequestDTO;
import ua.shpp.dto.branch.BranchResponseDTO;
import ua.shpp.dto.branch.WorkingHourDTO;
import ua.shpp.entity.BranchEntity;
import ua.shpp.entity.Organization;
import ua.shpp.exception.BranchAlreadyExistsException;
import ua.shpp.exception.BranchNotFoundException;
import ua.shpp.exception.BranchOrganizationMismatchException;
import ua.shpp.exception.OrganizationNotFound;
import ua.shpp.mapper.BranchMapper;
import ua.shpp.mapper.WorkingHourMapper;
import ua.shpp.model.WorkingHour;
import ua.shpp.repository.BranchRepository;
import ua.shpp.repository.OrganizationRepository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BranchServiceTests {

    private BranchService branchService;
    private BranchRepository branchRepository;
    private OrganizationRepository organizationRepository;
    private BranchMapper branchMapper;
    private WorkingHourMapper workingHourMapper;

    private BranchRequestDTO branchRequest;

    @BeforeEach
    void setUp() {
        branchRepository = mock(BranchRepository.class);
        organizationRepository = mock(OrganizationRepository.class);
        branchMapper = mock(BranchMapper.class);
        workingHourMapper = mock(WorkingHourMapper.class);

        branchService = new BranchService(organizationRepository, branchRepository, branchMapper, workingHourMapper);
        branchRequest = new BranchRequestDTO("branchName");
    }

    @Test
    void shouldThrowExceptionIfBranchAlreadyExists() {
        when(branchRepository.existsByName(branchRequest.name())).thenReturn(true);
        assertThrows(OrganizationNotFound.class, () -> branchService.create(1L, branchRequest));
    }

    @Test
    void shouldThrowExceptionIfOrganizationIsNotFound() {
        when(branchRepository.existsByName(branchRequest.name())).thenReturn(false);
        when(organizationRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(OrganizationNotFound.class, () -> branchService.create(1L, branchRequest));
    }

    @Test
    void shouldCreateBranch() {
        Organization org = new Organization();
        BranchEntity branchEntity = new BranchEntity();
        BranchResponseDTO responseDTO = mock(BranchResponseDTO.class);

        when(branchRepository.existsByName(branchRequest.name())).thenReturn(false);
        when(organizationRepository.findById(1L)).thenReturn(Optional.of(org));
        when(branchRepository.save(any(BranchEntity.class))).thenReturn(branchEntity);
        when(branchMapper.toResponseDTO(branchEntity)).thenReturn(responseDTO);

        BranchResponseDTO result = branchService.create(1L, branchRequest);
        assertEquals(responseDTO, result);
    }

    @Test
    void shouldThrowBranchNotFoundOnGet() {
        when(branchRepository.findById(10L)).thenReturn(Optional.empty());
        assertThrows(BranchNotFoundException.class, () -> branchService.get(1L, 10L));
    }

    @Test
    void shouldThrowMismatchException() {
        BranchEntity branch = new BranchEntity();
        Organization org = new Organization();
        org.setId(99L);

        branch.setOrganization(org);
        when(branchRepository.findById(1L)).thenReturn(Optional.of(branch));
        assertThrows(BranchOrganizationMismatchException.class, () -> branchService.get(1L, 1L));
    }

    @Test
    void shouldUpdateName() {
        BranchPatchRequestDTO patchRequest = new BranchPatchRequestDTO("newName");
        Organization org = new Organization();
        org.setId(1L);

        BranchEntity branch = new BranchEntity();
        branch.setOrganization(org);
        BranchResponseDTO responseDTO = mock(BranchResponseDTO.class);

        when(branchRepository.existsByName("newName")).thenReturn(false);
        when(branchRepository.findById(1L)).thenReturn(Optional.of(branch));
        when(branchRepository.save(any())).thenReturn(branch);
        when(branchMapper.toResponseDTO(branch)).thenReturn(responseDTO);

        BranchResponseDTO result = branchService.updateName(1L, 1L, patchRequest);
        assertEquals(responseDTO, result);
    }

    @Test
    void shouldUpdateWorkingHours() {
        Organization org = new Organization();
        org.setId(1L);

        BranchEntity branch = new BranchEntity();
        branch.setOrganization(org);

        WorkingHourDTO dto = new WorkingHourDTO(DayOfWeek.MONDAY, "09:00", "18:00", false);
        WorkingHour entity = new WorkingHour();
        BranchResponseDTO responseDTO = mock(BranchResponseDTO.class);

        when(branchRepository.findById(1L)).thenReturn(Optional.of(branch));
        when(workingHourMapper.toEntity(dto)).thenReturn(entity);
        when(branchRepository.save(any())).thenReturn(branch);
        when(branchMapper.toResponseDTO(branch)).thenReturn(responseDTO);

        BranchResponseDTO result = branchService.updateWorkingHours(1L, 1L, List.of(dto));
        assertEquals(responseDTO, result);
    }

    @Test
    void shouldDeleteBranch() {
        BranchEntity branch = new BranchEntity();
        Organization org = new Organization();
        org.setId(1L);
        branch.setOrganization(org);

        when(branchRepository.findById(1L)).thenReturn(Optional.of(branch));
        doNothing().when(branchRepository).delete(branch);

        assertDoesNotThrow(() -> branchService.delete(1L, 1L));
        verify(branchRepository, times(1)).delete(branch);
    }
}
