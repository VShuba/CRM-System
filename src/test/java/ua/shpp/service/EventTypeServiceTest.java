package ua.shpp.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import ua.shpp.dto.EventTypeRequestDTO;
import ua.shpp.dto.EventTypeResponseDTO;
import ua.shpp.entity.BranchEntity;
import ua.shpp.entity.EventTypeEntity;
import ua.shpp.entity.Organization;
import ua.shpp.exception.EntityNotFoundException;
import ua.shpp.exception.EventTypeAlreadyExistsException;
import ua.shpp.exception.EventTypeNotFoundException;
import ua.shpp.mapper.EventTypeMapper;
import ua.shpp.repository.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventTypeServiceTest {
    @Mock
    private EventTypeRepository eventTypeRepository;
    @Mock
    private EventTypeMapper eventTypeMapper;

    @Mock
    private ServiceRepository serviceRepository;
    @Mock
    private BranchRepository branchRepository;
    @Mock
    private OneTimeOfferRepository oneTimeOfferRepository;
    @Mock
    private SubscriptionOfferRepository subscriptionOfferRepository;

    @InjectMocks
    private EventTypeService eventTypeService;

    @Test
    void create_shouldSaveAndReturnDto() {
        // Arrange
        EventTypeRequestDTO requestDTO = mock(EventTypeRequestDTO.class);
        EventTypeEntity entityToSave = mock(EventTypeEntity.class);
        EventTypeEntity savedEntity = mock(EventTypeEntity.class);
        EventTypeResponseDTO expectedResponseDTO = mock(EventTypeResponseDTO.class);

        when(requestDTO.name()).thenReturn("Yoga");
        when(requestDTO.branchId()).thenReturn(1L);
        when(eventTypeRepository.existsByNameAndBranchId("Yoga", 1L)).thenReturn(false);
        when(eventTypeMapper.toEntity(eq(requestDTO), any(), any(), any(), any())).thenReturn(entityToSave);
        when(eventTypeRepository.save(entityToSave)).thenReturn(savedEntity);
        when(eventTypeMapper.toResponseDTO(savedEntity)).thenReturn(expectedResponseDTO);

        // Act
        EventTypeResponseDTO actualResponseDTO = eventTypeService.create(requestDTO);

        // Assert
        assertEquals(expectedResponseDTO, actualResponseDTO);
        verify(eventTypeRepository).existsByNameAndBranchId("Yoga", 1L);
        verify(eventTypeMapper).toEntity(eq(requestDTO), any(), any(), any(), any());
        verify(eventTypeRepository).save(entityToSave);
        verify(eventTypeMapper).toResponseDTO(savedEntity);
    }

    @Test
    void create_shouldThrow_whenDuplicateNameInBranch() {
        // Arrange
        EventTypeRequestDTO dto = mock(EventTypeRequestDTO.class);
        when(dto.name()).thenReturn("Yoga");
        when(dto.branchId()).thenReturn(1L);
        when(eventTypeRepository.existsByNameAndBranchId("Yoga", 1L)).thenReturn(true);

        // Act & Assert
        assertThrows(EventTypeAlreadyExistsException.class, () -> eventTypeService.create(dto));
        verify(eventTypeRepository).existsByNameAndBranchId("Yoga", 1L);
        verify(eventTypeMapper, never()).toEntity(any(), any(), any(), any(), any());
        verify(eventTypeRepository, never()).save(any());
    }

    @Test
    void getById_shouldReturnDto_whenExists() {
        // Arrange
        Long eventTypeId = 1L;
        EventTypeEntity foundEntity = mock(EventTypeEntity.class);
        EventTypeResponseDTO expectedDto = mock(EventTypeResponseDTO.class);

        when(eventTypeRepository.findById(eventTypeId)).thenReturn(Optional.of(foundEntity));
        when(eventTypeMapper.toResponseDTO(foundEntity)).thenReturn(expectedDto);

        // Act
        EventTypeResponseDTO actualDto = eventTypeService.getById(eventTypeId);

        // Assert
        assertEquals(expectedDto, actualDto);
        verify(eventTypeRepository).findById(eventTypeId);
        verify(eventTypeMapper).toResponseDTO(foundEntity);
    }

    @Test
    void getById_shouldThrow_whenNotFound() {
        // Arrange
        Long eventTypeId = 1L;
        when(eventTypeRepository.findById(eventTypeId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EventTypeNotFoundException.class, () -> eventTypeService.getById(eventTypeId));
        verify(eventTypeRepository).findById(eventTypeId);
        verify(eventTypeMapper, never()).toResponseDTO(any());
    }

    @Test
    void getAllByBranch_shouldReturnMappedDTOs() {
        // Arrange
        Long branchId = 1L;
        EventTypeEntity entity1 = mock(EventTypeEntity.class);
        EventTypeEntity entity2 = mock(EventTypeEntity.class);
        List<EventTypeEntity> entityList = List.of(entity1, entity2);

        EventTypeResponseDTO dto1 = mock(EventTypeResponseDTO.class);
        EventTypeResponseDTO dto2 = mock(EventTypeResponseDTO.class);
        List<EventTypeResponseDTO> expectedDtoList = List.of(dto1, dto2);

        when(eventTypeRepository.findAllByBranchId(branchId)).thenReturn(entityList);
        when(eventTypeMapper.toResponseDTO(entity1)).thenReturn(dto1);
        when(eventTypeMapper.toResponseDTO(entity2)).thenReturn(dto2);

        // Act
        List<EventTypeResponseDTO> actualDtoList = eventTypeService.getAllByBranch(branchId);

        // Assert
        assertEquals(expectedDtoList, actualDtoList);
        verify(eventTypeRepository).findAllByBranchId(branchId);
        verify(eventTypeMapper, times(entityList.size())).toResponseDTO(any(EventTypeEntity.class));
        verify(eventTypeMapper).toResponseDTO(entity1);
        verify(eventTypeMapper).toResponseDTO(entity2);
    }

    @Test
    void delete_shouldRemove_whenExists() {
        // Arrange
        Long eventTypeId = 1L;
        when(eventTypeRepository.existsById(eventTypeId)).thenReturn(true);

        // Act
        eventTypeService.delete(eventTypeId);

        // Assert
        verify(eventTypeRepository).existsById(eventTypeId);
        verify(eventTypeRepository).deleteById(eventTypeId);
    }

    @Test
    void delete_shouldThrow_whenNotExists() {
        // Arrange
        Long eventTypeId = 1L;
        when(eventTypeRepository.existsById(eventTypeId)).thenReturn(false);

        // Act & Assert
        assertThrows(EventTypeNotFoundException.class, () -> eventTypeService.delete(eventTypeId));
        verify(eventTypeRepository).existsById(eventTypeId);
        verify(eventTypeRepository, never()).deleteById(anyLong());
    }

    // === Helper methods for getFiltered tests ===
    private Page<EventTypeEntity> createMockEntityPage(List<EventTypeEntity> entities,
                                                       Pageable pageable, long totalElements) {
        return new PageImpl<>(entities, pageable, totalElements);
    }

    private List<EventTypeResponseDTO> setupMockMapperForEntities(List<EventTypeEntity> entities) {
        List<EventTypeResponseDTO> dtos = new ArrayList<>();
        for (EventTypeEntity entity : entities) {
            EventTypeResponseDTO dto = mock(EventTypeResponseDTO.class);
            when(eventTypeMapper.toResponseDTO(entity)).thenReturn(dto);
            dtos.add(dto);
        }
        return dtos;
    }

    private void setupRepositoryFindAll(Page<EventTypeEntity> entityPage, Pageable pageable) {
        when(eventTypeRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(entityPage);
    }

    @Test
    void getFiltered_shouldReturnMappedPage_whenAllFiltersApplied() {
        // Arrange
        String name = "yoga";
        Long branchId = 1L;
        Long serviceId = 2L;
        Pageable pageable = PageRequest.of(0, 10);

        List<EventTypeEntity> entityList = IntStream.range(0, 2)
                .mapToObj(i -> mock(EventTypeEntity.class))
                .toList();
        Page<EventTypeEntity> entityPage = createMockEntityPage(entityList, pageable, entityList.size());
        List<EventTypeResponseDTO> expectedDtoList = setupMockMapperForEntities(entityList);
        setupRepositoryFindAll(entityPage, pageable);

        // Act
        Page<EventTypeResponseDTO> actualResultPage = eventTypeService.getFiltered(name, branchId, serviceId, pageable);

        // Assert
        assertNotNull(actualResultPage);
        assertEquals(entityList.size(), actualResultPage.getContent().size());
        assertEquals(expectedDtoList, actualResultPage.getContent());
        assertEquals(pageable.getPageNumber(), actualResultPage.getNumber());
        assertEquals(pageable.getPageSize(), actualResultPage.getSize());
        assertEquals(entityList.size(), actualResultPage.getTotalElements());
        assertEquals(1, actualResultPage.getTotalPages());

        // Verify
        verify(eventTypeRepository).findAll(any(Specification.class), eq(pageable));
        verify(eventTypeMapper, times(entityList.size())).toResponseDTO(any(EventTypeEntity.class));
    }

    @Test
    void getFiltered_shouldReturnMappedPage_whenFilteringByName() {
        // Arrange
        String name = "event";
        Pageable pageable = PageRequest.of(0, 10);

        List<EventTypeEntity> entityList = IntStream.range(0, 1)
                .mapToObj(i -> mock(EventTypeEntity.class))
                .toList();
        Page<EventTypeEntity> entityPage = createMockEntityPage(entityList, pageable, entityList.size());
        List<EventTypeResponseDTO> expectedDtoList = setupMockMapperForEntities(entityList);
        setupRepositoryFindAll(entityPage, pageable);

        // Act
        Page<EventTypeResponseDTO> actualResultPage = eventTypeService.getFiltered(name,
                null, null, pageable);

        // Assert
        assertNotNull(actualResultPage);
        assertEquals(entityList.size(), actualResultPage.getContent().size());
        assertEquals(expectedDtoList, actualResultPage.getContent());

        // Verify interactions
        verify(eventTypeRepository).findAll(any(Specification.class), eq(pageable));
        verify(eventTypeMapper, times(entityList.size())).toResponseDTO(any(EventTypeEntity.class));
    }

    @Test
    void getFiltered_shouldReturnMappedPage_whenFilteringByBranchId() {
        // Arrange
        Long branchId = 5L;
        Pageable pageable = PageRequest.of(0, 10);

        List<EventTypeEntity> entityList = IntStream.range(0, 2)
                .mapToObj(i -> mock(EventTypeEntity.class))
                .toList();
        Page<EventTypeEntity> entityPage = createMockEntityPage(entityList, pageable, entityList.size());
        List<EventTypeResponseDTO> expectedDtoList = setupMockMapperForEntities(entityList);
        setupRepositoryFindAll(entityPage, pageable);

        // Act
        Page<EventTypeResponseDTO> actualResultPage = eventTypeService.getFiltered(null, branchId,
                null, pageable);

        // Assert
        assertNotNull(actualResultPage);
        assertEquals(entityList.size(), actualResultPage.getContent().size());
        assertEquals(expectedDtoList, actualResultPage.getContent());

        // Verify interactions
        verify(eventTypeRepository).findAll(any(Specification.class), eq(pageable));
        verify(eventTypeMapper, times(entityList.size())).toResponseDTO(any(EventTypeEntity.class));
    }

    @Test
    void getFiltered_shouldReturnMappedPage_whenFilteringByServiceId() {
        // Arrange
        Long serviceId = 7L;
        Pageable pageable = PageRequest.of(0, 10);

        List<EventTypeEntity> entityList = IntStream.range(0, 1)
                .mapToObj(i -> mock(EventTypeEntity.class))
                .toList();
        Page<EventTypeEntity> entityPage = createMockEntityPage(entityList, pageable, entityList.size());
        List<EventTypeResponseDTO> expectedDtoList = setupMockMapperForEntities(entityList);
        setupRepositoryFindAll(entityPage, pageable);

        // Act
        Page<EventTypeResponseDTO> actualResultPage = eventTypeService.getFiltered(null,
                null, serviceId, pageable);

        // Assert
        assertNotNull(actualResultPage);
        assertEquals(entityList.size(), actualResultPage.getContent().size());
        assertEquals(expectedDtoList, actualResultPage.getContent());

        // Verify interactions
        verify(eventTypeRepository).findAll(any(Specification.class), eq(pageable));
        verify(eventTypeMapper, times(entityList.size())).toResponseDTO(any(EventTypeEntity.class));
    }

    @Test
    void getFiltered_shouldReturnMappedPage_whenNoFiltersApplied() {
        // Arrange
        Pageable pageable = PageRequest.of(1, 20);

        List<EventTypeEntity> entityList = IntStream.range(0, 3)
                .mapToObj(i -> mock(EventTypeEntity.class))
                .toList();
        Page<EventTypeEntity> entityPage = createMockEntityPage(entityList, pageable, entityList.size());
        List<EventTypeResponseDTO> expectedDtoList = setupMockMapperForEntities(entityList);
        setupRepositoryFindAll(entityPage, pageable);

        // Act
        Page<EventTypeResponseDTO> actualResultPage = eventTypeService.getFiltered(null,
                null, null, pageable);

        // Assert
        assertNotNull(actualResultPage);
        assertEquals(entityList.size(), actualResultPage.getContent().size());
        assertEquals(expectedDtoList, actualResultPage.getContent());
        assertEquals(pageable.getPageNumber(), actualResultPage.getNumber());
        assertEquals(pageable.getPageSize(), actualResultPage.getSize());

        // Verify interactions
        verify(eventTypeRepository).findAll(any(Specification.class), eq(pageable));
        verify(eventTypeMapper, times(entityList.size())).toResponseDTO(any(EventTypeEntity.class));
    }

    @Test
    void getFiltered_shouldReturnEmptyPage_whenRepositoryReturnsEmptyPage() {
        // Arrange
        String name = "nonexistent";
        Long branchId = 99L;
        Pageable pageable = PageRequest.of(0, 10);

        Page<EventTypeEntity> emptyEntityPage = Page.empty(pageable);
        setupRepositoryFindAll(emptyEntityPage, pageable);

        // Act
        Page<EventTypeResponseDTO> actualResultPage = eventTypeService.getFiltered(name,
                branchId, null, pageable);

        // Assert
        assertNotNull(actualResultPage);
        assertTrue(actualResultPage.getContent().isEmpty());
        assertEquals(0, actualResultPage.getTotalElements());
        assertEquals(0, actualResultPage.getTotalPages());
        assertEquals(pageable.getPageNumber(), actualResultPage.getNumber());
        assertEquals(pageable.getPageSize(), actualResultPage.getSize());

        // Verify
        verify(eventTypeRepository).findAll(any(Specification.class), eq(pageable));
        verify(eventTypeMapper, never()).toResponseDTO(any(EventTypeEntity.class));
    }

    @Test
    void getFiltered_shouldTreatBlankNameAsNull() {
        // Arrange
        String name = "";
        Long branchId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        List<EventTypeEntity> entityList = IntStream.range(0, 1)
                .mapToObj(i -> mock(EventTypeEntity.class))
                .toList();
        Page<EventTypeEntity> entityPage = createMockEntityPage(entityList, pageable, entityList.size());
        List<EventTypeResponseDTO> expectedDtoList = setupMockMapperForEntities(entityList);
        setupRepositoryFindAll(entityPage, pageable);

        // Act
        Page<EventTypeResponseDTO> actualResultPage = eventTypeService.getFiltered(name, branchId,
                null, pageable);

        // Assert
        assertNotNull(actualResultPage);
        assertEquals(entityList.size(), actualResultPage.getContent().size());
        assertEquals(expectedDtoList, actualResultPage.getContent());

        // Verify
        verify(eventTypeRepository).findAll(any(Specification.class), eq(pageable));
        verify(eventTypeMapper, times(entityList.size())).toResponseDTO(any(EventTypeEntity.class));
    }

    @Test
    void update_shouldThrow_whenEntityNotFound() {
        // Arrange
        Long eventTypeId = 1L;
        EventTypeRequestDTO dto = mock(EventTypeRequestDTO.class);
        when(eventTypeRepository.findById(eventTypeId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EventTypeNotFoundException.class, () -> eventTypeService.update(eventTypeId, dto));
        verify(eventTypeRepository).findById(eventTypeId);
        verify(eventTypeMapper, never()).updateFromDto(any(), any(), any(), any(), any(), any());
        verify(eventTypeRepository, never()).save(any());
    }

    @Test
    void validateNameChangeUniqueness_nameChanged_branchChanged_exists_duplicate() {
        // Arrange
        String newName = "Yoga Class";
        Long newBranchId = 2L;
        String existingName = "Old Yoga Class";
        Long existingBranchId = 1L;

        BranchEntity existingBranch = mock(BranchEntity.class);
        when(existingBranch.getId()).thenReturn(existingBranchId);

        EventTypeEntity existingEventType = mock(EventTypeEntity.class);
        when(existingEventType.getName()).thenReturn(existingName);
        when(existingEventType.getBranch()).thenReturn(existingBranch);


        EventTypeRequestDTO dto = mock(EventTypeRequestDTO.class);
        when(dto.name()).thenReturn(newName);
        when(dto.branchId()).thenReturn(newBranchId);
        when(eventTypeRepository.existsByNameAndBranchId(newName, newBranchId)).thenReturn(true);

        // Act & Assert
        EventTypeAlreadyExistsException thrown = assertThrows(EventTypeAlreadyExistsException.class, () ->
                eventTypeService.validateNameChangeUniqueness(existingEventType, dto));

        assertTrue(thrown.getMessage().contains("Event type with name " + newName + " already exists in this branch"));
        verify(eventTypeRepository).existsByNameAndBranchId(newName, newBranchId);
    }

    @Test
    void validateNameChangeUniqueness_nameChanged_branchChanged_noDuplicate() {
        // Arrange
        String newName = "Yoga Class";
        Long newBranchId = 2L;
        String existingName = "Old Yoga Class";
        Long existingBranchId = 1L;

        BranchEntity existingBranch = mock(BranchEntity.class);
        when(existingBranch.getId()).thenReturn(existingBranchId);

        EventTypeEntity existingEventType = mock(EventTypeEntity.class);
        when(existingEventType.getName()).thenReturn(existingName);
        when(existingEventType.getBranch()).thenReturn(existingBranch);

        EventTypeRequestDTO dto = mock(EventTypeRequestDTO.class);
        when(dto.name()).thenReturn(newName);
        when(dto.branchId()).thenReturn(newBranchId);
        when(eventTypeRepository.existsByNameAndBranchId(newName, newBranchId)).thenReturn(false);

        // Act
        assertDoesNotThrow(() -> eventTypeService.validateNameChangeUniqueness(existingEventType, dto));

        // Verify
        verify(eventTypeRepository).existsByNameAndBranchId(newName, newBranchId);
    }

    @Test
    void validateNameChangeUniqueness_nameNotChanged_branchChanged_exists_duplicate() {
        // Arrange
        String existingName = "Yoga Class";
        Long existingBranchId = 1L;
        Long newBranchId = 2L;

        BranchEntity existingBranch = mock(BranchEntity.class);
        when(existingBranch.getId()).thenReturn(existingBranchId);

        EventTypeEntity existingEventType = mock(EventTypeEntity.class);
        when(existingEventType.getName()).thenReturn(existingName);
        when(existingEventType.getBranch()).thenReturn(existingBranch);


        EventTypeRequestDTO dto = mock(EventTypeRequestDTO.class);
        when(dto.name()).thenReturn(existingName);
        when(dto.branchId()).thenReturn(newBranchId);
        when(eventTypeRepository.existsByNameAndBranchId(existingName, newBranchId)).thenReturn(true);

        // Act & Assert
        EventTypeAlreadyExistsException thrown = assertThrows(EventTypeAlreadyExistsException.class, () ->
                eventTypeService.validateNameChangeUniqueness(existingEventType, dto));

        assertTrue(thrown.getMessage().contains("Event type with name " + existingName +
                " already exists in this branch"));
        verify(eventTypeRepository).existsByNameAndBranchId(existingName, newBranchId);
    }

    @Test
    void validateNameChangeUniqueness_nameNotChanged_branchNotChanged_noDuplicate() {
        // Arrange
        String existingName = "Yoga Class";
        Long existingBranchId = 1L;

        BranchEntity existingBranch = mock(BranchEntity.class);
        when(existingBranch.getId()).thenReturn(existingBranchId);

        EventTypeEntity existingEventType = mock(EventTypeEntity.class);
        when(existingEventType.getName()).thenReturn(existingName);
        when(existingEventType.getBranch()).thenReturn(existingBranch);


        EventTypeRequestDTO dto = mock(EventTypeRequestDTO.class);
        when(dto.name()).thenReturn(existingName);
        when(dto.branchId()).thenReturn(existingBranchId);

        // Act
        assertDoesNotThrow(() -> eventTypeService.validateNameChangeUniqueness(existingEventType, dto));

        // Verify
        verify(eventTypeRepository, never()).existsByNameAndBranchId(existingName, existingBranchId);
    }


    @Test
    void update_shouldUpdateAndReturnDto_whenNameAndBranchAreValidAndUnique() {
        // Arrange
        Long eventTypeId = 1L;
        Long oldBranchId = 1L;
        Long newBranchId = 2L;
        String oldName = "Old Event";
        String newName = "New Unique Event";

        EventTypeRequestDTO requestDTO = mock(EventTypeRequestDTO.class);
        when(requestDTO.name()).thenReturn(newName);
        when(requestDTO.branchId()).thenReturn(newBranchId);

        Organization organization = mock(Organization.class);

        BranchEntity currentBranch = mock(BranchEntity.class);
        when(currentBranch.getId()).thenReturn(oldBranchId);
        when(currentBranch.getOrganization()).thenReturn(organization);

        BranchEntity newBranch = mock(BranchEntity.class);
        when(newBranch.getOrganization()).thenReturn(organization);

        EventTypeEntity existingEntity = mock(EventTypeEntity.class);
        when(existingEntity.getId()).thenReturn(eventTypeId);
        when(existingEntity.getName()).thenReturn(oldName);
        when(existingEntity.getBranch()).thenReturn(currentBranch);

        EventTypeEntity savedEntity = mock(EventTypeEntity.class);
        EventTypeResponseDTO responseDTO = mock(EventTypeResponseDTO.class);

        when(eventTypeRepository.findById(eventTypeId)).thenReturn(Optional.of(existingEntity));
        when(branchRepository.findById(newBranchId)).thenReturn(Optional.of(newBranch));
        when(eventTypeRepository.existsByNameAndBranchId(newName, newBranchId)).thenReturn(false);
        when(eventTypeRepository.save(existingEntity)).thenReturn(savedEntity);
        when(eventTypeMapper.toResponseDTO(savedEntity)).thenReturn(responseDTO);

        doNothing().when(eventTypeMapper).updateFromDto(eq(requestDTO), eq(existingEntity),
                any(), any(), any(), any());

        // Act
        EventTypeResponseDTO result = eventTypeService.update(eventTypeId, requestDTO);

        // Assert
        assertEquals(responseDTO, result);
        verify(eventTypeRepository).findById(eventTypeId);
        verify(branchRepository).findById(newBranchId);
        verify(eventTypeRepository).existsByNameAndBranchId(newName, newBranchId);
        verify(eventTypeRepository).save(existingEntity);
        verify(eventTypeMapper).toResponseDTO(savedEntity);
    }

    @Test
    void update_shouldUpdateAndReturnDto_whenNameAndBranchUnchanged() {
        Long eventTypeId = 1L;
        Long branchId = 1L;
        String name = "Event Name";
        Long orgId = 100L;

        EventTypeRequestDTO requestDTO = mock(EventTypeRequestDTO.class);
        when(requestDTO.name()).thenReturn(name);
        when(requestDTO.branchId()).thenReturn(branchId);

        Organization organization = mock(Organization.class);
        when(organization.getId()).thenReturn(orgId);

        BranchEntity branch = mock(BranchEntity.class);
        when(branch.getId()).thenReturn(branchId);
        when(branch.getOrganization()).thenReturn(organization);

        EventTypeEntity existing = mock(EventTypeEntity.class);
        when(existing.getId()).thenReturn(eventTypeId);
        when(existing.getName()).thenReturn(name);
        when(existing.getBranch()).thenReturn(branch);

        EventTypeEntity updated = mock(EventTypeEntity.class);
        EventTypeResponseDTO dto = mock(EventTypeResponseDTO.class);

        when(eventTypeRepository.findById(eventTypeId)).thenReturn(Optional.of(existing));
        when(branchRepository.findById(branchId)).thenReturn(Optional.of(branch));
        when(eventTypeRepository.save(existing)).thenReturn(updated);
        when(eventTypeMapper.toResponseDTO(updated)).thenReturn(dto);

        doNothing().when(eventTypeMapper).updateFromDto(eq(requestDTO), eq(existing),
                any(), any(), any(), any());

        // Act
        EventTypeResponseDTO result = eventTypeService.update(eventTypeId, requestDTO);

        // Assert
        assertEquals(dto, result);
        verify(eventTypeRepository, never()).existsByNameAndBranchId(anyString(), anyLong());
    }

    @Test
    void update_shouldThrowAlreadyExists_whenNameConflictExists() {
        Long eventTypeId = 1L;
        Long branchId = 1L;
        String newName = "Duplicate Name";
        Long orgId = 99L;

        Organization organization = mock(Organization.class);
        when(organization.getId()).thenReturn(orgId);

        BranchEntity branch = mock(BranchEntity.class);
        when(branch.getId()).thenReturn(branchId);
        when(branch.getOrganization()).thenReturn(organization);

        EventTypeEntity existing = mock(EventTypeEntity.class);
        when(existing.getName()).thenReturn("Old Name");
        when(existing.getBranch()).thenReturn(branch);

        EventTypeRequestDTO dto = mock(EventTypeRequestDTO.class);
        when(dto.name()).thenReturn(newName);
        when(dto.branchId()).thenReturn(branchId);

        when(eventTypeRepository.findById(eventTypeId)).thenReturn(Optional.of(existing));
        when(branchRepository.findById(branchId)).thenReturn(Optional.of(branch));
        when(eventTypeRepository.existsByNameAndBranchId(newName, branchId)).thenReturn(true);

        // Act + Assert
        assertThrows(EventTypeAlreadyExistsException.class, () ->
                eventTypeService.update(eventTypeId, dto));
    }

    @Test
    void update_shouldThrowEntityNotFound_whenNewBranchNotFound() {
        Long eventTypeId = 1L;
        Long missingBranchId = 999L;

        Organization org = mock(Organization.class);

        BranchEntity currentBranch = mock(BranchEntity.class);
        when(currentBranch.getOrganization()).thenReturn(org);

        EventTypeEntity existing = mock(EventTypeEntity.class);
        when(existing.getBranch()).thenReturn(currentBranch);

        EventTypeRequestDTO dto = mock(EventTypeRequestDTO.class);
        when(dto.branchId()).thenReturn(missingBranchId);

        when(eventTypeRepository.findById(eventTypeId)).thenReturn(Optional.of(existing));
        when(branchRepository.findById(missingBranchId)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(EntityNotFoundException.class, () ->
                eventTypeService.update(eventTypeId, dto));
    }

    @Test
    void update_shouldThrowAccessDeniedException_whenBranchBelongsToDifferentOrganization() {
        // Arrange
        Long eventTypeId = 1L;
        Long currentOrgId = 100L;
        Long newOrgId = 200L;
        Long newBranchId = 2L;

        EventTypeRequestDTO requestDTO = mock(EventTypeRequestDTO.class);
        when(requestDTO.branchId()).thenReturn(newBranchId);

        Organization currentOrg = mock(Organization.class);
        when(currentOrg.getId()).thenReturn(currentOrgId);

        BranchEntity currentBranch = mock(BranchEntity.class);
        when(currentBranch.getOrganization()).thenReturn(currentOrg);

        EventTypeEntity existingEntity = mock(EventTypeEntity.class);
        when(existingEntity.getBranch()).thenReturn(currentBranch);

        Organization newOrg = mock(Organization.class);
        when(newOrg.getId()).thenReturn(newOrgId);

        BranchEntity newBranch = mock(BranchEntity.class);
        when(newBranch.getOrganization()).thenReturn(newOrg);

        when(eventTypeRepository.findById(eventTypeId)).thenReturn(Optional.of(existingEntity));
        when(branchRepository.findById(newBranchId)).thenReturn(Optional.of(newBranch));

        // Act & Assert
        AccessDeniedException exception = assertThrows(AccessDeniedException.class, () ->
                eventTypeService.update(eventTypeId, requestDTO));

        assertEquals("Cannot assign event type to a branch in another organization", exception.getMessage());
        verify(eventTypeRepository).findById(eventTypeId);
        verify(branchRepository).findById(newBranchId);
    }
}