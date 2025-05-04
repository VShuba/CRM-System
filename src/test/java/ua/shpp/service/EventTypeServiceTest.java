package ua.shpp.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import ua.shpp.dto.EventTypeRequestDTO;
import ua.shpp.dto.EventTypeResponseDTO;
import ua.shpp.entity.BranchEntity;
import ua.shpp.entity.EventTypeEntity;
import ua.shpp.exception.EventTypeAlreadyExistsException;
import ua.shpp.exception.EventTypeNotFoundException;
import ua.shpp.mapper.EventTypeMapper;
import ua.shpp.repository.BranchRepository;
import ua.shpp.repository.EventTypeRepository;

import java.util.List;
import java.util.Optional;

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

    @InjectMocks
    private EventTypeService eventTypeService;

    @Test
    void create_shouldSaveAndReturnDto() {
        EventTypeRequestDTO requestDTO = mock(EventTypeRequestDTO.class);
        EventTypeEntity entity = mock(EventTypeEntity.class);
        EventTypeEntity savedEntity = mock(EventTypeEntity.class);
        EventTypeResponseDTO responseDTO = mock(EventTypeResponseDTO.class);

        when(requestDTO.name()).thenReturn("Yoga");
        when(requestDTO.branchId()).thenReturn(1L);
        when(eventTypeRepository.existsByNameAndBranchId("Yoga", 1L)).thenReturn(false);
        when(eventTypeMapper.toEntity(eq(requestDTO), any(), any(), any(), any())).thenReturn(entity);
        when(eventTypeRepository.save(entity)).thenReturn(savedEntity);
        when(eventTypeMapper.toResponseDTO(savedEntity)).thenReturn(responseDTO);

        EventTypeResponseDTO result = eventTypeService.create(requestDTO);

        assertEquals(responseDTO, result);
    }

    @Test
    void create_shouldThrow_whenDuplicateNameInBranch() {
        EventTypeRequestDTO dto = mock(EventTypeRequestDTO.class);
        when(dto.name()).thenReturn("Yoga");
        when(dto.branchId()).thenReturn(1L);
        when(eventTypeRepository.existsByNameAndBranchId("Yoga", 1L)).thenReturn(true);

        assertThrows(EventTypeAlreadyExistsException.class, () -> eventTypeService.create(dto));
    }

    @Test
    void getById_shouldReturnDto_whenExists() {
        EventTypeEntity entity = mock(EventTypeEntity.class);
        EventTypeResponseDTO dto = mock(EventTypeResponseDTO.class);

        when(eventTypeRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(eventTypeMapper.toResponseDTO(entity)).thenReturn(dto);

        EventTypeResponseDTO result = eventTypeService.getById(1L);

        assertEquals(dto, result);
    }

    @Test
    void getById_shouldThrow_whenNotFound() {
        when(eventTypeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EventTypeNotFoundException.class, () -> eventTypeService.getById(1L));
    }

    @Test
    void getAllByBranch_shouldReturnMappedDtos() {
        Long branchId = 1L;
        EventTypeEntity entity = mock(EventTypeEntity.class);
        EventTypeResponseDTO dto = mock(EventTypeResponseDTO.class);

        when(eventTypeRepository.findAllByBranchId(branchId)).thenReturn(List.of(entity));
        when(eventTypeMapper.toResponseDTO(entity)).thenReturn(dto);

        List<EventTypeResponseDTO> result = eventTypeService.getAllByBranch(branchId);

        assertEquals(List.of(dto), result);
    }

    @Test
    void delete_shouldRemove_whenExists() {
        when(eventTypeRepository.existsById(1L)).thenReturn(true);

        eventTypeService.delete(1L);

        verify(eventTypeRepository).deleteById(1L);
    }

    @Test
    void delete_shouldThrow_whenNotExists() {
        when(eventTypeRepository.existsById(1L)).thenReturn(false);

        assertThrows(EventTypeNotFoundException.class, () -> eventTypeService.delete(1L));
    }

    @Test
    void getFiltered_shouldReturnMappedPage() {
        Pageable pageable = PageRequest.of(0, 10);
        EventTypeEntity entity = mock(EventTypeEntity.class);
        EventTypeResponseDTO dto = mock(EventTypeResponseDTO.class);
        Page<EventTypeEntity> page = new PageImpl<>(List.of(entity));

        when(eventTypeRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);
        when(eventTypeMapper.toResponseDTO(entity)).thenReturn(dto);

        Page<EventTypeResponseDTO> result = eventTypeService.getFiltered("yoga", 1L, 2L, pageable);

        assertEquals(1, result.getContent().size());
        assertEquals(dto, result.getContent().get(0));
    }

    @Test
    void update_shouldThrow_whenEntityNotFound() {
        EventTypeRequestDTO dto = mock(EventTypeRequestDTO.class);
        when(eventTypeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EventTypeNotFoundException.class, () -> eventTypeService.update(1L, dto));
    }
}