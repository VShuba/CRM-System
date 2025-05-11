package ua.shpp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import ua.shpp.entity.payment.SubscriptionDealEntity;

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
public class SubscriptionOfferEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
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

    @Column(nullable = false)
    private Long price;

    public void setTermOfValidityInDays(Integer termOfValidityInDays) {
        this.termOfValidityInDays = Period.ofDays(termOfValidityInDays);
    }

    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private EventTypeEntity eventType;


    @OneToMany(
            mappedBy = "subscriptionService",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<SubscriptionDealEntity> subscriptionDeal = new ArrayList<>();
}
