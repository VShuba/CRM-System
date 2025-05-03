package ua.shpp.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ua.shpp.dto.EventClientDto;
import ua.shpp.entity.ClientEntity;
import ua.shpp.entity.EventClientEntity;
import ua.shpp.entity.EventClientId;
import ua.shpp.entity.ScheduleEventEntity;
import ua.shpp.entity.payment.OneTimeInfoEntity;
import ua.shpp.entity.payment.SubscriptionInfoEntity;
import ua.shpp.exception.ClientNotFoundException;
import ua.shpp.exception.DealNotFoundException;
import ua.shpp.exception.EventNotFoundException;
import ua.shpp.repository.ClientRepository;
import ua.shpp.repository.OneTimeInfoRepository;
import ua.shpp.repository.ScheduleEventRepository;
import ua.shpp.repository.SubscriptionInfoRepository;

@Mapper(componentModel = "spring")
public interface EventClientMapper {

    @Mapping(source = "eventUserId.clientId", target = "clientId")
    @Mapping(source = "scheduleEvent.id", target = "scheduleId")
    @Mapping(source = "oneTimeInfo.id", target = "oneTimeInfoId")
    @Mapping(source = "subscriptionInfo.id", target = "subscriptionInfoId")
    EventClientDto toDto(EventClientEntity entity);


@Mapping(
        target     = "eventUserId",
        expression = "java(toEmbeddedId(dto.clientId(), dto.scheduleId()))"
)
@Mapping(target = "subscriptionInfo", source = "subscriptionInfoId",
        qualifiedByName = "idToSubscription")
@Mapping(target = "oneTimeInfo", source = "oneTimeInfoId",
        qualifiedByName = "idToOneTimeInfo")
@Mapping(target = "scheduleEvent", source = "scheduleId",
        qualifiedByName = "idToScheduleEvent")
@Mapping(target = "client", source = "clientId",
        qualifiedByName = "idToClient")
    EventClientEntity toEntity(EventClientDto dto,
                               @Context SubscriptionInfoRepository subscriptionInfoRepository,
                               @Context OneTimeInfoRepository oneTimeInfoRepository,
                               @Context ScheduleEventRepository scheduleEventRepository,
                               @Context ClientRepository clientRepository
                               );

    @Named("toEmbeddedId")
    default EventClientId toEmbeddedId(Long clientId, Long eventId) {
        return new EventClientId(eventId, clientId);
    }

    @Named("idToSubscription")
    default SubscriptionInfoEntity toEmbeddedId(Long id,
                                                @Context SubscriptionInfoRepository repository) {
        return repository.findById(id).orElseThrow(
                () -> new DealNotFoundException(String.format(" Subscription id: %s, not found", id))
        );
    }

    @Named("idToOneTimeInfo")
    default OneTimeInfoEntity idToOneTimeInfo(Long id,
                                              @Context OneTimeInfoRepository repository) {
        return repository.findById(id).orElseThrow(
                () -> new DealNotFoundException(String.format(" One-time visit id: %s, not found", id))
        );
    }

    @Named("idToScheduleEvent")
    default ScheduleEventEntity idToScheduleEvent(Long id,
                                                  @Context ScheduleEventRepository repository) {
        return repository.findById(id).orElseThrow(
                () -> new EventNotFoundException(String.format("Event id: %d, not found", id))
        );
    }

    @Named("idToClient")
    default ClientEntity idToClient(Long id,
                                           @Context ClientRepository repository) {
        return repository.findById(id).orElseThrow(
                () ->  new ClientNotFoundException(id)
        );
    }
}
