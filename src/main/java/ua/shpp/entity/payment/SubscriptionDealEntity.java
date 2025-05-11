package ua.shpp.entity.payment;

import jakarta.persistence.*;
import lombok.*;
import ua.shpp.entity.ClientEntity;
import ua.shpp.entity.SubscriptionOfferEntity;

import java.time.LocalDate;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "subscription_deal")
public non-sealed class SubscriptionDealEntity implements Checkable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private ClientEntity client;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subscriptionService_id", nullable = false)
    private SubscriptionOfferEntity subscriptionService;

    @Column(nullable = false)
    private Integer visits;

    @Column(name = "expiration_date", nullable = false, updatable = false)
    private LocalDate expirationDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "check_id", referencedColumnName = "id", nullable = false)
    private CheckEntity paymentCheck;

    @PrePersist
    private void setExpirationDate() {
        expirationDate = LocalDate.now()
                .plusDays(subscriptionService
                        .getTermOfValidityInDays()
                        .getDays());
    }
}
