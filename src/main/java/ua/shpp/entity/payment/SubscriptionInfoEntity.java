package ua.shpp.entity.payment;

import jakarta.persistence.*;
import lombok.*;
import ua.shpp.entity.ClientEntity;
import ua.shpp.entity.SubscriptionServiceEntity;

import java.time.LocalDate;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "subscription_info")
public non-sealed class SubscriptionInfoEntity implements Checkable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private ClientEntity client;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subscriptionService_id", nullable = false)
    private SubscriptionServiceEntity subscriptionService;

    @Column(nullable = false)
    private Byte visitsUsed;

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
