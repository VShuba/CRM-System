package ua.shpp.entity.payment;

import jakarta.persistence.*;
import lombok.*;
import ua.shpp.entity.ClientEntity;
import ua.shpp.entity.OneTimeOfferEntity;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "one_time_deal")
public non-sealed class OneTimeDealEntity implements Checkable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private ClientEntity client;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "oneTimeService_id", nullable = false)
    private OneTimeOfferEntity oneTimeService;

    @Column(nullable = false)
    private Boolean visitUsed;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "check_id", referencedColumnName = "id", nullable = false)
    private CheckEntity paymentCheck;
}
