package ua.shpp.mapper;

import org.springframework.stereotype.Component;
import ua.shpp.dto.ServiceResponseDTO;
import ua.shpp.entity.EmployeeEntity;
import ua.shpp.entity.RoomEntity;
import ua.shpp.entity.ServiceEntity;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ServiceEntityToDTOMapper {

    public ServiceResponseDTO toResponse(ServiceEntity entity) {
        String branchName = entity.getBranch() != null ? entity.getBranch().getName() : null;

        Set<String> roomNames = entity.getRooms() != null
                ? entity.getRooms().stream()
                .map(RoomEntity::getName)
                .collect(Collectors.toSet())
                : Set.of();

        Set<String> employeeNames = entity.getEmployees() != null
                ? entity.getEmployees().stream()
                .map(EmployeeEntity::getName)
                .collect(Collectors.toSet())
                : Set.of();

        return new ServiceResponseDTO(
                entity.getId(),
                entity.getName(),
                entity.getColor(),
                branchName,
                roomNames,
                employeeNames
        );
    }
}