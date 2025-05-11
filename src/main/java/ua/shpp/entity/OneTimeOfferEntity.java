package ua.shpp.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import ua.shpp.entity.payment.OneTimeDealEntity;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "one_time_services")
public class OneTimeOfferEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "activity_id", nullable = false)
    private ServiceEntity activity;

    @Column(name = "duration_in_minutes", nullable = false)
    private Duration durationInMinutes;

    @Column(name = "price", nullable = false)
    private Long price;

    public void setDurationInMinutes(long durationInMinutes) {
        this.durationInMinutes = Duration.ofMinutes(durationInMinutes);
    }

    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private EventTypeEntity eventType;


    @OneToMany(
            mappedBy = "oneTimeService",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private List<OneTimeDealEntity> oneTimeDeal = new ArrayList<>();
}
