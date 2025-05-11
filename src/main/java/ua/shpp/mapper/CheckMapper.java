package ua.shpp.mapper;


import org.mapstruct.*;
import ua.shpp.dto.CheckDto;
import ua.shpp.entity.payment.CheckEntity;
import ua.shpp.repository.OneTimeDealRepository;
import ua.shpp.repository.SubscriptionDealRepository;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface CheckMapper {

    CheckDto toDto(CheckEntity entity);

    @Mapping(target = "oneTimeInfo", ignore = true)
    @Mapping(target = "subscriptionInfo", ignore = true)
    CheckEntity toEntity(CheckDto dto, @Context OneTimeDealRepository oneTimeDealRepository, @Context SubscriptionDealRepository subscriptionDealRepository);

    @AfterMapping
    default void setBranch(CheckDto dto,@MappingTarget CheckEntity entity,
                           @Context OneTimeDealRepository oneTimeDealRepository,
                           @Context SubscriptionDealRepository subscriptionDealRepository) {
        entity.setOneTimeInfo(oneTimeDealRepository.getByPaymentCheckId(dto.id()).orElse(null));
        entity.setSubscriptionInfo(subscriptionDealRepository.getByPaymentCheckId(dto.id()).orElse(null));
    }
}
