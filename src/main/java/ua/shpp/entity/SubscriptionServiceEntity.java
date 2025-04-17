package ua.shpp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "subscription_services")
public class SubscriptionServiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToMany
    @JoinTable(
            name = "subscription_activity",
            joinColumns = @JoinColumn(name = "subscription_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "activity_id", nullable = false)
    )
    private List<ServiceEntity> activities = new ArrayList<>();

    @Column(nullable = false)
    private Integer visits;

    @Column(name = "term_of_validity_in_day", nullable = false)
    private Period termOfValidityInDays = Period.ofDays(35);

    private Long price;

    public Period getTermOfValidityInDays() {
        return termOfValidityInDays;
    }

    public void setTermOfValidityInDays(Integer termOfValidityInDays) {
        this.termOfValidityInDays = Period.ofDays(termOfValidityInDays);
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_type_id", nullable = false)
    private EventTypeEntity eventType;
}
