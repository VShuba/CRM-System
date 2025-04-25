package ua.shpp.mapper;

import org.mapstruct.*;
import ua.shpp.dto.branch.room.RoomRequestDTO;
import ua.shpp.dto.branch.room.RoomResponseDTO;
import ua.shpp.entity.BranchEntity;
import ua.shpp.entity.RoomEntity;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface RoomMapper {
    RoomResponseDTO toDTO(RoomEntity source);

    @Mapping(target = "branch", ignore = true)
    RoomEntity toEntity(RoomRequestDTO source, @Context BranchEntity branchEntity);

    @AfterMapping
    default void setBranch(@MappingTarget RoomEntity roomEntity, @Context BranchEntity branchEntity) {
        roomEntity.setBranch(branchEntity);
    }
}
