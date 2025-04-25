package ua.shpp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ua.shpp.dto.EmployeeRequestDTO;
import ua.shpp.dto.EmployeeResponseDTO;
import ua.shpp.entity.EmployeeEntity;
import ua.shpp.entity.ServiceEntity;

import java.util.List;
import java.util.Set;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface EmployeeMapper {

    EmployeeEntity EmployeeRequestDTOToEmployeeEntity(EmployeeRequestDTO requestDTO);

    @Mapping(target = "base64Avatar", source = "base64Avatar")
    @Mapping(target = "branchId", source = "employeeEntity.branch.id")
    @Mapping(target = "serviceIds", source = "employeeEntity.services", qualifiedByName = "mapServicesToIds")
    EmployeeResponseDTO employeeEntityToEmployeeResponseDTO(EmployeeEntity employeeEntity, String base64Avatar);

    @Named("mapServicesToIds")
    default List<Long> mapServiceEntitiesToIds(Set<ServiceEntity> employeeEntities) {
        return employeeEntities.stream()
                .map(ServiceEntity::getId)
                .toList();
    }
}