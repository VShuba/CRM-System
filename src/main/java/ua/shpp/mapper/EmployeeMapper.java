package ua.shpp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.shpp.dto.EmployeeRequestDTO;
import ua.shpp.dto.EmployeeResponseDTO;
import ua.shpp.entity.EmployeeEntity;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface EmployeeMapper {

    EmployeeEntity EmployeeRequestDTOToEmployeeEntity(EmployeeRequestDTO requestDTO);

    @Mapping(target = "base64Avatar", source = "base64Avatar")
    EmployeeResponseDTO EmployeeEntityToEmployeeResponseDTO(EmployeeEntity employeeEntity, String base64Avatar);
}