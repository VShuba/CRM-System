package ua.shpp.entity.payment;

import jakarta.persistence.*;
import lombok.*;
import ua.shpp.entity.ClientEntity;
import ua.shpp.entity.OneTimeServiceEntity;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "one_time_info")
public class OneTimeInfoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private ClientEntity client;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "oneTimeService_id", nullable = false)
    private OneTimeServiceEntity oneTimeService;

    @Column(nullable = false)
    private Boolean visitUsed;

    @Column(nullable = false)
    private Boolean paid;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "check_id", referencedColumnName = "id", nullable = false)
    private CheckEntity paymentCheck;
}
