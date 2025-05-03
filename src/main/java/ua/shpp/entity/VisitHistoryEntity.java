package ua.shpp.entity;

import jakarta.persistence.*;
import lombok.*;
import ua.shpp.model.PaymentMethodForStory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "visit_history")
@Builder
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class VisitHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private ClientEntity client;

    @Column(name = "service_color", nullable = false, length = 20)
    private String serviceColor;

    @Column(name = "service_name", nullable = false, length = 100)
    private String serviceName;

    @Column(name = "trainer_full_name", length = 150)
    private String trainerFullName;

    @Column(name = "room_name", length = 100)
    private String roomName;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "time", nullable = false)
    private LocalTime time;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethodForStory paymentMethodForStory;

    @Column(name = "amount_paid")
    private BigDecimal amountPaid;
}
