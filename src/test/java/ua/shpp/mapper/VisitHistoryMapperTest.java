package ua.shpp.mapper;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mapstruct.factory.Mappers;
import ua.shpp.dto.VisitHistoryDTO;
import ua.shpp.entity.*;
import ua.shpp.entity.payment.CheckEntity;
import ua.shpp.entity.payment.OneTimeInfoEntity;
import ua.shpp.entity.payment.SubscriptionInfoEntity;
import ua.shpp.model.PaymentMethod;
import ua.shpp.model.PaymentMethodForStory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class VisitHistoryMapperTest {

    private final VisitHistoryMapper visitHistoryMapper = Mappers.getMapper(VisitHistoryMapper.class);

    @Test
    void toVisitHistoryEntity_shouldMapFieldsCorrectly() {
        // given
        EmployeeEntity trainer = new EmployeeEntity();
        trainer.setName("Іван Тренер");

        RoomEntity room = new RoomEntity();
        room.setName("Зал 1");

        ServiceEntity service = new ServiceEntity();
        service.setName("Йога");
        service.setColor("Зелений");

        ScheduleEventEntity schedule = new ScheduleEventEntity();
        schedule.setTrainer(trainer);
        schedule.setRoom(room);
        schedule.setServiceEntity(service);
        schedule.setEventDate(LocalDate.of(2024, 4, 1));
        schedule.setStartTime(LocalTime.of(10, 0));

        ClientEntity client = new ClientEntity();
        client.setId(42L);

        EventClientEntity eventClient = new EventClientEntity();
        eventClient.setClient(client);
        eventClient.setScheduleEvent(schedule);

        SubscriptionInfoEntity subscriptionInfo = new SubscriptionInfoEntity();
        eventClient.setSubscriptionInfo(subscriptionInfo);

        // when
        VisitHistoryEntity result = visitHistoryMapper.toVisitHistoryEntity(eventClient);

        // then
        assertThat(result.getId()).isNull();
        assertThat(result.getClientId()).isEqualTo(42L);
        assertThat(result.getServiceName()).isEqualTo("Йога");
        assertThat(result.getServiceColor()).isEqualTo("Зелений");
        assertThat(result.getTrainerFullName()).isEqualTo("Іван Тренер");
        assertThat(result.getDate()).isEqualTo(LocalDate.of(2024, 4, 1));
        assertThat(result.getTime()).isEqualTo(LocalTime.of(10, 0));
        assertThat(result.getRoomName()).isEqualTo("Зал 1");
        assertThat(result.getPaymentMethodForStory()).isEqualTo(PaymentMethodForStory.SUBSCRIPTION);
        assertThat(result.getAmountPaid()).isNull();
    }

    @ParameterizedTest
    @CsvSource({"CARD, 100", "CASH, 100"})
    void toVisitHistoryEntity_withOneTimePayment_shouldMapCorrectly(PaymentMethod method, BigDecimal amount) {
        CheckEntity check = new CheckEntity();
        check.setPaymentMethod(method);
        check.setPrice(amount);

        OneTimeInfoEntity oneTimeInfo = new OneTimeInfoEntity();
        oneTimeInfo.setPaymentCheck(check);

        EventClientEntity eventClient = new EventClientEntity();
        eventClient.setOneTimeInfo(oneTimeInfo);
        eventClient.setClient(new ClientEntity());
        eventClient.setScheduleEvent(new ScheduleEventEntity());

        VisitHistoryEntity result = visitHistoryMapper.toVisitHistoryEntity(eventClient);

        assertThat(result.getPaymentMethodForStory()).isEqualTo(PaymentMethodForStory.valueOf(method.name()));
        assertThat(result.getAmountPaid()).isEqualTo(amount);
    }

    @Test
    void toVisitHistoryEntity_withSubscription_shouldMapPaymentMethodAndNullAmount() {
        // given
        SubscriptionInfoEntity subscriptionInfo = new SubscriptionInfoEntity();

        EventClientEntity eventClient = new EventClientEntity();
        eventClient.setSubscriptionInfo(subscriptionInfo);
        eventClient.setClient(new ClientEntity());
        eventClient.setScheduleEvent(new ScheduleEventEntity());

        // when
        VisitHistoryEntity result = visitHistoryMapper.toVisitHistoryEntity(eventClient);

        // then
        assertThat(result.getPaymentMethodForStory()).isEqualTo(PaymentMethodForStory.SUBSCRIPTION);
        assertThat(result.getAmountPaid()).isNull();
    }

    @Test
    void toDtoList_shouldMapListCorrectly() {
        VisitHistoryEntity entity1 = new VisitHistoryEntity();
        entity1.setClientId(1L);

        VisitHistoryEntity entity2 = new VisitHistoryEntity();
        entity2.setClientId(2L);

        List<VisitHistoryDTO> dtoList = visitHistoryMapper.toDtoList(List.of(entity1, entity2));

        assertThat(dtoList).hasSize(2);
        assertThat(dtoList.get(0).clientId()).isEqualTo(1L);
        assertThat(dtoList.get(1).clientId()).isEqualTo(2L);
    }
}