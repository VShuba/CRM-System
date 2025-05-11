package ua.shpp.mapper;

import org.mapstruct.*;
import ua.shpp.dto.SubscriptionDealRequestDto;
import ua.shpp.dto.SubscriptionDealResponseDto;
import ua.shpp.entity.ClientEntity;
import ua.shpp.entity.SubscriptionOfferEntity;
import ua.shpp.entity.payment.CheckEntity;
import ua.shpp.entity.payment.SubscriptionDealEntity;
import ua.shpp.exception.CheckNotFoundException;
import ua.shpp.exception.ClientNotFoundException;
import ua.shpp.exception.OfferNotFoundException;
import ua.shpp.repository.CheckRepository;
import ua.shpp.repository.ClientRepository;
import ua.shpp.repository.SubscriptionOfferRepository;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface SubscriptionDealMapper {
    @Mapping(target = "clientId", source = "client",
            qualifiedByName = "clientToId")
    @Mapping(target = "subscriptionId", source = "subscriptionService",
            qualifiedByName = "subscriptionToId")
    @Mapping(target = "checkId", source = "paymentCheck",
            qualifiedByName = "checkToId")
    SubscriptionDealResponseDto toDto(SubscriptionDealEntity entity);

    @Mapping(target = "client", source = "clientId",
            qualifiedByName = "idToClient")
    @Mapping(target = "subscriptionService", source = "subscriptionId",
            qualifiedByName = "idToOffer")
    SubscriptionDealEntity toEntity(SubscriptionDealRequestDto dto,
                                    @Context ClientRepository clientRepository,
                                    @Context SubscriptionOfferRepository offerRepository,
                                    @Context CheckRepository checkRepository);


    @Named("clientToId")
    static Long clientToId(ClientEntity entity) {
        return entity != null ? entity.getId() : null;
    }

    @Named("subscriptionToId")
    static Long subscriptionToId(SubscriptionOfferEntity entity) {
        return entity != null ? entity.getId() : null;
    }

    @Named("checkToId")
    static Long checkToId(CheckEntity entity) {
        return entity != null ? entity.getId() : null;
    }

    @Named("idToClient")
    default ClientEntity idToClient(Long id,
                                    @Context ClientRepository repository) {
        return repository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException(id));
    }

    @Named("idToOffer")
    default SubscriptionOfferEntity idToOffer(Long id,
                                              @Context SubscriptionOfferRepository repository) {
        return repository.findById(id)
                .orElseThrow(() -> new OfferNotFoundException(String.format("Offer id: %d, not found", id)));
    }

    @Named("idToCheck")
    default CheckEntity idToCheck(Long id,
                                  @Context CheckRepository repository) {
        return repository.findById(id)
                .orElseThrow(() -> new CheckNotFoundException(String.format("Check id: %d, not found", id)));
    }

    @AfterMapping
    default void setBranch(SubscriptionDealRequestDto dto, @MappingTarget SubscriptionDealEntity entity,
                           @Context SubscriptionOfferRepository offerRepository) {
        entity.setVisits(
                offerRepository
                        .findById(
                                dto.subscriptionId())
                        .orElseThrow(
                                () -> new OfferNotFoundException(
                                        String.format("Offer id: %d, not found", dto.subscriptionId())))
                        .getVisits()
        );
    }
}
