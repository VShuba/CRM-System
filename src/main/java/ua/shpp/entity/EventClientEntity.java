package ua.shpp.entity;

import jakarta.persistence.*;
import lombok.*;
import ua.shpp.entity.payment.OneTimeDealEntity;
import ua.shpp.entity.payment.SubscriptionDealEntity;
import ua.shpp.model.ClientEventStatus;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode(of = "eventUserId")
@Table(name = "event_clients")
public class EventClientEntity {
    @EmbeddedId
    private EventClientId eventUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("clientId")
    @JoinColumn(name = "client_id")
    private ClientEntity client;

    @ManyToOne
    @MapsId("eventId")
    @JoinColumn(name = "event_id")
    private ScheduleEventEntity scheduleEvent;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private ClientEventStatus clientEventStatus;

    // Додано, щоб зв’язати відвідування конкретної події з придбаним OneTimeDealEntity
    @ManyToOne
    @JoinColumn(name = "one_time_info_id")
    private OneTimeDealEntity oneTimeInfo;

    // Додано, щоб зв’язати відвідування конкретної події з SubscriptionDealEntity
    @ManyToOne
    @JoinColumn(name = "subscription_info_id")
    private SubscriptionDealEntity subscriptionInfo;
}
