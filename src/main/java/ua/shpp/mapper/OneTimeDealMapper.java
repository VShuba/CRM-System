package ua.shpp.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ua.shpp.dto.OneTimeDealRequestDto;
import ua.shpp.dto.OneTimeDealResponseDto;
import ua.shpp.entity.ClientEntity;
import ua.shpp.entity.OneTimeOfferEntity;
import ua.shpp.entity.payment.CheckEntity;
import ua.shpp.entity.payment.OneTimeDealEntity;
import ua.shpp.exception.CheckNotFoundException;
import ua.shpp.exception.ClientNotFoundException;
import ua.shpp.exception.OfferNotFoundException;
import ua.shpp.repository.CheckRepository;
import ua.shpp.repository.ClientRepository;
import ua.shpp.repository.OneTimeOfferRepository;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface OneTimeDealMapper {
    @Mapping(target = "clientId", source = "client",
            qualifiedByName = "clientToId")
    @Mapping(target = "oneTimeId", source = "oneTimeService",
            qualifiedByName = "oneTimeToId")
    @Mapping(target = "checkId", source = "paymentCheck",
            qualifiedByName = "checkToId")
    OneTimeDealResponseDto toDto(OneTimeDealEntity entity);

    @Mapping(target = "client", source = "clientId",
            qualifiedByName = "idToClient")
    @Mapping(target = "oneTimeService", source = "oneTimeId",
            qualifiedByName = "idToOneTime")
//    @Mapping(target = "paymentCheck", source = "checkId",
//            qualifiedByName = "idToCheck")
    OneTimeDealEntity toEntity(OneTimeDealRequestDto dto,
                               @Context ClientRepository clientRepository,
                               @Context OneTimeOfferRepository oneTimeOfferRepository,
                               @Context CheckRepository checkRepository);

    @Named("clientToId")
    static Long clientToId(ClientEntity entity) {
        return entity != null ? entity.getId() : null;
    }

    @Named("idToClient")
    default ClientEntity idToClient(Long id,
                                    @Context ClientRepository repository) {
        return repository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException(id));
    }

    @Named("oneTimeToId")
    static Long oneTimeToId(OneTimeOfferEntity entity) {
        return entity != null ? entity.getId() : null;
    }

    @Named("idToOneTime")
    default OneTimeOfferEntity idToOneTime(Long id,
                                           @Context OneTimeOfferRepository repository) {
        return repository.findById(id)
                .orElseThrow(() -> new OfferNotFoundException(String.format("Offer id: %d, not found", id)));
    }

    @Named("checkToId")
    static Long checkToId(CheckEntity entity) {
        return entity != null ? entity.getId() : null;
    }

    @Named("idToCheck")
    default CheckEntity idToCheck(Long id,
                                  @Context CheckRepository repository) {
        return repository.findById(id)
                .orElseThrow(() -> new CheckNotFoundException(String.format("Check id: %d, not found", id)));
    }
}
