package ua.shpp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ua.shpp.dto.ServiceRequestDTO;
import ua.shpp.dto.ServiceResponseDTO;
import ua.shpp.entity.BranchEntity;
import ua.shpp.entity.RoomEntity;
import ua.shpp.entity.ServiceEntity;
import ua.shpp.exception.ServiceNotFoundException;
import ua.shpp.mapper.ServiceEntityToDTOMapper;
import ua.shpp.repository.BranchRepository;
import ua.shpp.repository.RoomRepository;
import ua.shpp.repository.ServiceRepository;

@Service
@RequiredArgsConstructor
public class ServiceService {

    private final ServiceRepository repository;
    private final BranchRepository branchRepository;
    private final RoomRepository roomRepository;
    private final ServiceEntityToDTOMapper mapper;

    public ResponseEntity<ServiceResponseDTO> create(ServiceRequestDTO dto) {
        BranchEntity branch = branchRepository.findById(dto.branchId())
                .orElseThrow(() -> new ServiceNotFoundException("Branch not found"));

        RoomEntity room = null;
        if (dto.roomId() != null) {
            room = roomRepository.findById(dto.roomId())
                    .orElseThrow(() -> new ServiceNotFoundException("Room not found"));
        }

        ServiceEntity service = ServiceEntity.builder()
                .name(dto.name())
                .color(dto.color())
                .branch(branch)
                .room(room) // може це виїздний pool-dance ?)))
                .build();

        repository.save(service);

        return new ResponseEntity<>(mapper.toResponse(service), HttpStatus.CREATED);
    }

    public ResponseEntity<ServiceResponseDTO> get(Long id) {
        ServiceEntity service = repository.findById(id)
                .orElseThrow(() -> new ServiceNotFoundException("Service not found"));

        return new ResponseEntity<>(mapper.toResponse(service), HttpStatus.OK);
    }

    public ResponseEntity<ServiceResponseDTO> update(Long id, ServiceRequestDTO dto) {
        ServiceEntity service = repository.findById(id)
                .orElseThrow(() -> new ServiceNotFoundException("Service not found"));

        BranchEntity branch = branchRepository.findById(dto.branchId())
                .orElseThrow(() -> new ServiceNotFoundException("Branch not found"));

        RoomEntity room = roomRepository.findById(dto.roomId())
                .orElseThrow(() -> new ServiceNotFoundException("Room not found"));

        service.setName(dto.name());
        service.setColor(dto.color());
        service.setBranch(branch);
        service.setRoom(room);

        repository.save(service);

        return new ResponseEntity<>(mapper.toResponse(service), HttpStatus.OK);
    }

    public void delete(Long id) {
        ServiceEntity service = repository.findById(id)
                .orElseThrow(() -> new ServiceNotFoundException("Service not found"));

        repository.delete(service);
    }
}