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
@NoArgsConstructor
@AllArgsConstructor
public class VisitHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "client_id", nullable = false)
    private Long clientId;

    @Column(name = "service_color", nullable = false)
    private String serviceColor;

    @Column(name = "service_name", nullable = false)
    private String serviceName;

    @Column(name = "trainer_full_name")
    private String trainerFullName;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "time", nullable = false)
    private LocalTime time;

    @Column(name = "room_name")
    private String roomName;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethodForStory paymentMethodForStory;

    @Column(name = "amount_paid")
    private BigDecimal amountPaid;
}
