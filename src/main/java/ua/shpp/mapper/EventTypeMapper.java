package ua.shpp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ua.shpp.dto.EventTypeRequestDTO;
import ua.shpp.dto.EventTypeResponseDTO;
import ua.shpp.entity.EventTypeEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {OneTimeOfferMapper.class,
        SubscriptionOfferMapper.class})
public interface EventTypeMapper {
    EventTypeEntity toEntity(EventTypeRequestDTO dto);
    EventTypeResponseDTO toResponseDTO(EventTypeEntity entity);
}
