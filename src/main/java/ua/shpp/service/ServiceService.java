package ua.shpp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.shpp.dto.ServiceRequestDTO;
import ua.shpp.dto.ServiceResponseDTO;
import ua.shpp.entity.BranchEntity;
import ua.shpp.entity.EmployeeEntity;
import ua.shpp.entity.RoomEntity;
import ua.shpp.entity.ServiceEntity;
import ua.shpp.exception.BranchNotFoundException;
import ua.shpp.exception.RoomNotFoundException;
import ua.shpp.exception.ServiceNotFoundException;
import ua.shpp.mapper.ServiceEntityToDTOMapper;
import ua.shpp.repository.BranchRepository;
import ua.shpp.repository.EmployeeRepository;
import ua.shpp.repository.RoomRepository;
import ua.shpp.repository.ServiceRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ServiceService {

    private final ServiceRepository repository;
    private final BranchRepository branchRepository;
    private final RoomRepository roomRepository;
    private final EmployeeRepository employeeRepository;
    private final ServiceEntityToDTOMapper mapper;

    public ServiceResponseDTO create(ServiceRequestDTO dto) {
        BranchEntity branch = getBranchById(dto.branchId());
        Set<RoomEntity> rooms = getRoomsByIds(dto.roomIds());
        Set<EmployeeEntity> employees = getEmployeesByIds(dto.employeeIds());

        ServiceEntity service = ServiceEntity.builder()
                .name(dto.name())
                .color(dto.color())
                .branch(branch)
                .rooms(rooms)
                .employees(employees)
                .build();

        repository.save(service);
        return mapper.toResponse(service);
    }

    public ServiceResponseDTO get(Long id) {
        ServiceEntity service = repository.findById(id)
                .orElseThrow(() -> new ServiceNotFoundException("Service not found"));
        return mapper.toResponse(service);
    }

    public ServiceResponseDTO update(Long id, ServiceRequestDTO dto) {
        ServiceEntity service = repository.findById(id)
                .orElseThrow(() -> new ServiceNotFoundException("Service not found"));

        BranchEntity branch = getBranchById(dto.branchId());
        Set<RoomEntity> rooms = getRoomsByIds(dto.roomIds());
        Set<EmployeeEntity> employees = getEmployeesByIds(dto.employeeIds());

        service.setName(dto.name());
        service.setColor(dto.color());
        service.setBranch(branch);
        service.setRooms(rooms);
        service.setEmployees(employees);

        repository.save(service);
        return mapper.toResponse(service);
    }

    public void delete(Long id) {
        ServiceEntity service = repository.findById(id)
                .orElseThrow(() -> new ServiceNotFoundException("Service not found"));

        repository.delete(service);
    }

    public Page<ServiceResponseDTO> getAll(Long branchId, Pageable pageable) {

        Page<ServiceEntity> allByBranchId = repository.findAllByBranchId(getBranchById(branchId).getId(), pageable);
        return allByBranchId.map(mapper::toResponse);
    }

    // Отримуємо філію за ID
    private BranchEntity getBranchById(Long id) {
        return branchRepository.findById(id)
                .orElseThrow(() -> new BranchNotFoundException("Branch not found"));
    }

    // Отримуємо кімнати за списком ID
    private Set<RoomEntity> getRoomsByIds(List<Long> roomIds) {
        if (roomIds == null || roomIds.isEmpty()) {
            return new HashSet<>();
        }
        Set<RoomEntity> rooms = new HashSet<>(roomRepository.findAllById(roomIds));
        if (rooms.size() != roomIds.size()) {
            throw new RoomNotFoundException("One or more rooms not found");
        }
        return rooms;
    }

    // Отримуємо співробітників за списком ID
    private Set<EmployeeEntity> getEmployeesByIds(List<Long> employeeIds) {
        if (employeeIds == null || employeeIds.isEmpty()) {
            return new HashSet<>();
        }
        Set<EmployeeEntity> employees = new HashSet<>(employeeRepository.findAllById(employeeIds));
        if (employees.size() != employeeIds.size()) {
            throw new ServiceNotFoundException("One or more employees not found");
        }
        return employees;
    }
}
