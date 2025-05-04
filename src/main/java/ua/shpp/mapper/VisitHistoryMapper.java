package ua.shpp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import ua.shpp.dto.VisitHistoryDTO;
import ua.shpp.entity.EmployeeEntity;
import ua.shpp.entity.EventClientEntity;
import ua.shpp.entity.RoomEntity;
import ua.shpp.entity.VisitHistoryEntity;
import ua.shpp.entity.payment.CheckEntity;
import ua.shpp.model.ClientEventStatus;
import ua.shpp.model.PaymentMethod;
import ua.shpp.model.PaymentMethodForStory;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface VisitHistoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "client", source = "client")
    @Mapping(target = "serviceColor", source = "scheduleEvent.serviceEntity.color")
    @Mapping(target = "serviceName", source = "scheduleEvent.serviceEntity.name")
    @Mapping(target = "trainerFullName", source = "scheduleEvent.employee", qualifiedByName = "trainerToFullName")
    @Mapping(target = "date", source = "scheduleEvent.eventDate")
    @Mapping(target = "time", source = "scheduleEvent.startTime")
    @Mapping(target = "roomName", source = "scheduleEvent.room", qualifiedByName = "roomToName")
    @Mapping(target = "paymentMethodForStory", source = "eventClientEntity", qualifiedByName = "determinePaymentMethod")
    @Mapping(target = "amountPaid", source = "eventClientEntity", qualifiedByName = "determineAmountPaid")
    VisitHistoryEntity toVisitHistoryEntity(EventClientEntity eventClientEntity);

    @Mapping(target = "clientId", source = "client.id")
    VisitHistoryDTO toDto(VisitHistoryEntity entity);

    List<VisitHistoryDTO> toDtoList(List<VisitHistoryEntity> entityList);

    @Named("trainerToFullName")
    default String trainerToFullName(EmployeeEntity trainer) {
        if (trainer == null) {
            return null;
        }
        return trainer.getName();
    }

    @Named("roomToName")
    default String roomToName(RoomEntity room) {
        if (room == null) {
            return null;
        }
        return room.getName();
    }

    @Named("determinePaymentMethod")
    default PaymentMethodForStory determinePaymentMethod(EventClientEntity eventClientEntity) {
        if (eventClientEntity.getClientEventStatus() == ClientEventStatus.SKIPPED) {
            return PaymentMethodForStory.SKIPPED;
        } else if (eventClientEntity.getSubscriptionInfo() != null) {
            return PaymentMethodForStory.SUBSCRIPTION;
        } else if (eventClientEntity.getOneTimeInfo() != null) {
            CheckEntity paymentCheck = eventClientEntity.getOneTimeInfo().getPaymentCheck();

            if (paymentCheck != null) {
                PaymentMethod paymentMethodFromCheck = paymentCheck.getPaymentMethod();
                if (paymentMethodFromCheck != null) {
                    try {
                        return PaymentMethodForStory.valueOf(paymentMethodFromCheck.name());
                    } catch (IllegalArgumentException e) {
                        return null;
                    }
                }
            }
        }
        return null;
    }

    @Named("determineAmountPaid")
    default BigDecimal determineAmountPaid(EventClientEntity eventClientEntity) {
        if (eventClientEntity.getOneTimeInfo() != null) {
            CheckEntity paymentCheck = eventClientEntity.getOneTimeInfo().getPaymentCheck();
            if (paymentCheck != null) {
                return paymentCheck.getPrice();
            }
        }
        return null;
    }
}
