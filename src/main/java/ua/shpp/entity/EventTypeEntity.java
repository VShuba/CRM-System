package ua.shpp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Table(name = "event_type")
public class EventTypeEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @ManyToMany(
            cascade = CascadeType.ALL
            , fetch = FetchType.LAZY
    )
    @JoinTable(
            name = "event_type_one_time_offers",
            joinColumns = @JoinColumn(name = "event_type_id"),
            inverseJoinColumns = @JoinColumn(name = "one_time_offer_id")
    )
    private List<OneTimeServiceEntity> oneTimeVisits = new ArrayList<>();

    @ManyToMany(
            cascade = CascadeType.ALL
            , fetch = FetchType.LAZY
    )
    @JoinTable(
            name = "event_type_subscriptions_offers",
            joinColumns = @JoinColumn(name = "event_type_id"),
            inverseJoinColumns = @JoinColumn(name = "subscriptions_offer_id")
    )
    private List<SubscriptionServiceEntity> subscriptions = new ArrayList<>();

    @PreRemove
    private void preRemove() {
        for (OneTimeServiceEntity oneTimeService : new ArrayList<>(oneTimeVisits)) {
            oneTimeService.getEventType().remove(this);
        }

        for (SubscriptionServiceEntity subscriptionService : new ArrayList<>(subscriptions)) {
            subscriptionService.getEventType().remove(this);
        }
    }

    @OneToMany(mappedBy = "eventType",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @Column(name = "schedule_event_id")
    private List<ScheduleEventEntity> scheduleEventEntities;

    @ManyToOne
    @JoinColumn(name = "branch_id", nullable = false)
    private BranchEntity branch;
}
