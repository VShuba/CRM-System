package ua.shpp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.shpp.dto.EventClientDto;
import ua.shpp.entity.EventClientEntity;

@Mapper(componentModel = "spring")
public interface EventClientMapper {

    @Mapping(source = "eventUserId.clientId", target = "clientId")
    @Mapping(source = "eventUserId.eventId", target = "scheduleId")
    @Mapping(source = "oneTimeInfo.id", target = "oneTimeInfoId")
    @Mapping(source = "subscriptionInfo.id", target = "subscriptionInfoId")
    EventClientDto toDto(EventClientEntity entity);

    @Mapping(source = "clientId", target = "eventUserId.clientId")
    @Mapping(source = "scheduleId", target = "eventUserId.eventId")
    @Mapping(source = "oneTimeInfoId", target = "oneTimeInfo.id")
    @Mapping(source = "subscriptionInfoId", target = "subscriptionInfo.id")
    EventClientEntity toEntity(EventClientDto dto);
}
