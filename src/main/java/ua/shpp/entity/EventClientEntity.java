package ua.shpp.entity;

import jakarta.persistence.*;
import lombok.*;
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

}
