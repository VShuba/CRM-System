package ua.shpp.entity;

import jakarta.persistence.*;
import lombok.*;
import ua.shpp.entity.payment.OneTimeInfoEntity;
import ua.shpp.entity.payment.SubscriptionInfoEntity;
import ua.shpp.model.ClientEventStatus;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode()
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
    private ClientEventStatus clientEventStatus;

    // Додано, щоб зв’язати відвідування конкретної події з придбаним OneTimeInfoEntity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "one_time_info_id")
    private OneTimeInfoEntity oneTimeInfo;

    // Додано, щоб зв’язати відвідування конкретної події з SubscriptionInfoEntity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_info_id")
    private SubscriptionInfoEntity subscriptionInfo;
}
