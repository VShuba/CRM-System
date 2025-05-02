package ua.shpp.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import ua.shpp.entity.payment.OneTimeInfoEntity;

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
public class OneTimeServiceEntity {
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

    @ManyToMany(
            mappedBy = "oneTimeVisits",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<EventTypeEntity> eventType = new ArrayList<>();

    @PreRemove
    private void preRemove() {
        for (EventTypeEntity type : new ArrayList<>(eventType)) {
            type.getOneTimeVisits().remove(this);
        }
    }

    @OneToMany(
            mappedBy = "oneTimeService",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private List<OneTimeInfoEntity> oneTimeInfo = new ArrayList<>();
}
