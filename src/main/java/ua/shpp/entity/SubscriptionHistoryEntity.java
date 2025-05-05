package ua.shpp.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Table(name = "subscription_history")
public class SubscriptionHistoryEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private ClientEntity client;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "eventType", nullable = false)
    private String eventType;

    @Column(name = "total_visits", nullable = false)
    private Integer totalVisits;

    @Column(name = "visits_left", nullable = false)
    private Integer visitsLeft;

    @Column(name = "is_valid", nullable = false)
    private Boolean isValid;
}
