package ua.shpp.mapper;

import org.mapstruct.Mapper;
import ua.shpp.dto.RoomResponseDTO;
import ua.shpp.entity.RoomEntity;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface RoomEntityToRoomDTOMapper {
    RoomResponseDTO roomEntityToRoomDTO(RoomEntity source);
}
