package ua.shpp.mapper;

import org.mapstruct.*;
import ua.shpp.dto.SubscriptionHistoryDTO;
import ua.shpp.entity.ClientEntity;
import ua.shpp.entity.SubscriptionHistoryEntity;
import ua.shpp.entity.SubscriptionOfferEntity;
import ua.shpp.entity.payment.SubscriptionDealEntity;
import ua.shpp.exception.ClientNotFoundException;
import ua.shpp.repository.ClientRepository;

import java.time.LocalDate;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SubscriptionHistoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "client", source = "subscriptionInfo.client")
    @Mapping(target = "name", source = "subscriptionService.name")
    @Mapping(target = "eventType", source = "subscriptionService.eventType.name")
    @Mapping(target = "totalVisits", source = "subscriptionService.visits")
    @Mapping(target = "visitsLeft", source = "subscriptionInfo.visits")
    @Mapping(target = "isValid", expression = "java(isStillValid(subscriptionInfo))")
    SubscriptionHistoryEntity toHistory(SubscriptionDealEntity subscriptionInfo,
                                        SubscriptionOfferEntity subscriptionService);

    @Mapping(target = "clientId", source = "client.id")
    SubscriptionHistoryDTO toDto(SubscriptionHistoryEntity entity);

    @Mapping(target = "client", source = "clientId", qualifiedByName = "idToClient")
    SubscriptionHistoryEntity toEntity(SubscriptionHistoryDTO dto, @Context ClientRepository clientRepository);

    default boolean isStillValid(SubscriptionDealEntity subscriptionInfo) {
        return subscriptionInfo.getVisits() > 0 &&
                subscriptionInfo.getExpirationDate().isAfter(LocalDate.now());
    }

    @Named("idToClient")
    default ClientEntity idToClient(Long id, @Context ClientRepository repository) {
        return repository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException(id));
    }
}
