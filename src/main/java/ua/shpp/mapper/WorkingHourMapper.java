package ua.shpp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.shpp.dto.branch.WorkingHourDTO;
import ua.shpp.model.WorkingHour;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface WorkingHourMapper {
    @Mapping(target = "dayOfWeek", source = "dayOfWeek")
    WorkingHourDTO toDto(WorkingHour source);

    @Mapping(target = "dayOfWeek", source = "dayOfWeek")
    WorkingHour toEntity(WorkingHourDTO source);
}
