package ua.shpp.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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


}
