package ua.shpp.mapper;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mapstruct.factory.Mappers;
import ua.shpp.dto.VisitHistoryDTO;
import ua.shpp.entity.*;
import ua.shpp.entity.payment.CheckEntity;
import ua.shpp.entity.payment.OneTimeDealEntity;
import ua.shpp.entity.payment.SubscriptionDealEntity;
import ua.shpp.model.ClientEventStatus;
import ua.shpp.model.PaymentMethod;
import ua.shpp.model.PaymentMethodForStory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class VisitHistoryMapperTest {

    private final VisitHistoryMapper visitHistoryMapper = Mappers.getMapper(VisitHistoryMapper.class);

    private EventClientEntity createBaseEntity() {
        EventClientEntity entity = new EventClientEntity();

        ServiceEntity service = new ServiceEntity();
        service.setName("Pilates");
        service.setColor("#FFFFFF");

        EmployeeEntity trainer = new EmployeeEntity();
        trainer.setName("John Doe");

        RoomEntity room = new RoomEntity();
        room.setName("Main Hall");

        ScheduleEventEntity scheduleEvent = new ScheduleEventEntity();
        scheduleEvent.setServiceEntity(service);
        scheduleEvent.setEmployee(trainer);
        scheduleEvent.setRoom(room);
        scheduleEvent.setEventDate(LocalDate.of(2024, 5, 1));
        scheduleEvent.setStartTime(LocalTime.of(10, 30));

        entity.setScheduleEvent(scheduleEvent);
        entity.setClient(new ClientEntity());
        entity.setClientEventStatus(ClientEventStatus.ASSIGNED);

        return entity;
    }

    @Test
    void toVisitHistoryEntity_shouldMapFieldsCorrectly() {
        // Arrange
        EventClientEntity eventClient = createBaseEntity();
        eventClient.setSubscriptionInfo(new SubscriptionDealEntity());

        // Act
        VisitHistoryEntity result = visitHistoryMapper.toVisitHistoryEntity(eventClient);

        // Assert
        assertThat(result.getId()).isNull();
        assertThat(result.getServiceName()).isEqualTo("Pilates");
        assertThat(result.getServiceColor()).isEqualTo("#FFFFFF");
        assertThat(result.getTrainerFullName()).isEqualTo("John Doe");
        assertThat(result.getDate()).isEqualTo(LocalDate.of(2024, 5, 1));
        assertThat(result.getTime()).isEqualTo(LocalTime.of(10, 30));
        assertThat(result.getRoomName()).isEqualTo("Main Hall");
        assertThat(result.getPaymentMethodForStory()).isEqualTo(PaymentMethodForStory.SUBSCRIPTION);
        assertThat(result.getAmountPaid()).isNull();
    }

    @ParameterizedTest
    @CsvSource({"CARD, 100", "CASH, 150"})
    void toVisitHistoryEntity_withOneTimePayment_shouldMapCorrectly(PaymentMethod method, BigDecimal amount) {
        EventClientEntity entity = createBaseEntity();

        CheckEntity check = new CheckEntity();
        check.setPaymentMethod(method);
        check.setPrice(amount);

        OneTimeDealEntity oneTimeInfo = new OneTimeDealEntity();
        oneTimeInfo.setPaymentCheck(check);
        entity.setOneTimeInfo(oneTimeInfo);

        VisitHistoryEntity result = visitHistoryMapper.toVisitHistoryEntity(entity);

        assertThat(result.getPaymentMethodForStory()).isEqualTo(PaymentMethodForStory.valueOf(method.name()));
        assertThat(result.getAmountPaid()).isEqualTo(amount);
    }

    @Test
    void testToVisitHistoryEntity_Skipped() {
        EventClientEntity entity = createBaseEntity();
        entity.setClientEventStatus(ClientEventStatus.SKIPPED);

        VisitHistoryEntity result = visitHistoryMapper.toVisitHistoryEntity(entity);

        assertThat(result.getPaymentMethodForStory()).isEqualTo(PaymentMethodForStory.SKIPPED);
        assertThat(result.getAmountPaid()).isNull();
    }

    @Test
    void testToVisitHistoryEntity_WithUnknownPaymentMethod_ShouldReturnNull() {
        EventClientEntity entity = createBaseEntity();

        CheckEntity check = new CheckEntity();
        check.setPaymentMethod(null);

        OneTimeDealEntity oneTimeInfo = new OneTimeDealEntity();
        oneTimeInfo.setPaymentCheck(check);
        entity.setOneTimeInfo(oneTimeInfo);

        VisitHistoryEntity result = visitHistoryMapper.toVisitHistoryEntity(entity);

        assertThat(result.getPaymentMethodForStory()).isNull();
        assertThat(result.getAmountPaid()).isNull();
    }

    @Test
    void testToVisitHistoryEntity_NoPaymentInfo_ShouldReturnNulls() {
        EventClientEntity entity = createBaseEntity();
        entity.setOneTimeInfo(null);
        entity.setSubscriptionInfo(null);

        VisitHistoryEntity result = visitHistoryMapper.toVisitHistoryEntity(entity);

        assertThat(result.getPaymentMethodForStory()).isNull();
        assertThat(result.getAmountPaid()).isNull();
    }

    @Test
    void testToDto() {
        VisitHistoryEntity entity = new VisitHistoryEntity();
        entity.setServiceName("Yoga");
        entity.setAmountPaid(BigDecimal.TEN);

        VisitHistoryDTO dto = visitHistoryMapper.toDto(entity);

        assertThat(dto.serviceName()).isEqualTo("Yoga");
        assertThat(dto.amountPaid()).isEqualTo(BigDecimal.TEN);
    }

    @Test
    void toDtoList_shouldMapListCorrectly() {
        // Arrange
        ClientEntity client1 = new ClientEntity();
        client1.setId(1L);

        ClientEntity client2 = new ClientEntity();
        client2.setId(2L);

        VisitHistoryEntity entity1 = new VisitHistoryEntity();
        entity1.setClient(client1);

        VisitHistoryEntity entity2 = new VisitHistoryEntity();
        entity2.setClient(client2);

        // Act
        List<VisitHistoryDTO> dtoList = visitHistoryMapper.toDtoList(List.of(entity1, entity2));

        // Assert
        assertThat(dtoList).hasSize(2);
        assertThat(dtoList.get(0).clientId()).isEqualTo(1L);
        assertThat(dtoList.get(1).clientId()).isEqualTo(2L);
    }
}