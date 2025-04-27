package ua.shpp.mapper;


import org.mapstruct.*;
import ua.shpp.dto.CheckDto;
import ua.shpp.entity.payment.CheckEntity;
import ua.shpp.repository.OneTimeInfoRepository;
import ua.shpp.repository.SubscriptionInfoRepository;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface CheckMapper {

    CheckDto toDto(CheckEntity entity);

    @Mapping(target = "oneTimeInfo", ignore = true)
    @Mapping(target = "subscriptionInfo", ignore = true)
    CheckEntity toEntity(CheckDto dto, @Context OneTimeInfoRepository oneTimeInfoRepository, @Context SubscriptionInfoRepository subscriptionInfoRepository);

    @AfterMapping
    default void setBranch(CheckDto dto,@MappingTarget CheckEntity entity,
                           @Context OneTimeInfoRepository oneTimeInfoRepository,
                           @Context SubscriptionInfoRepository subscriptionInfoRepository) {
        entity.setOneTimeInfo(oneTimeInfoRepository.getByPaymentCheckId(dto.id()).orElse(null));
        entity.setSubscriptionInfo(subscriptionInfoRepository.getByPaymentCheckId(dto.id()).orElse(null));
    }
}
