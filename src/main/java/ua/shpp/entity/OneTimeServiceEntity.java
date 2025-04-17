package ua.shpp.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "one_time_services")
public class OneTimeServiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "activity_id", nullable = false)
//    private Activity activity;

    @Column(name = "duration_in_minutes", nullable = false)
    private Duration durationInMinutes;

    @Column(nullable = false)
    private Long price;

    public Duration getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(long durationInMinutes) {
        this.durationInMinutes = Duration.ofMinutes(durationInMinutes);
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_type_id", nullable = false)
    private EventTypeEntity eventType;
}
