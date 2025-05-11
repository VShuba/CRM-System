package ua.shpp.entity.payment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import ua.shpp.model.PaymentMethod;
import ua.shpp.util.CheckNumberGeneratorUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "payment_checks")
public class CheckEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "check_number", nullable = false, updatable = false, length = 20)
    private String checkNumber;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt; // є така аноташка @CreationTimestamp може її?

    @Column(name = "organization_name", nullable = false, updatable = false, length = 100)
    private String organizationName;

    @Column(name = "branch_address", nullable = false, updatable = false, length = 100)
    private String branchAddress;

    @Column(name = "branch_phone_number", nullable = false, updatable = false, length = 20)
    private String branchPhoneNumber;

    @Column(name = "customer_name", nullable = false, updatable = false, length = 120)
    private String customerName;

    @Column(name = "customer_phone_number", nullable = false, updatable = false, length = 20)
    private String customerPhoneNumber;

    @Column(name = "offer_name", nullable = false, updatable = false, length = 100)
    private String offerName;

    @Column(name = "price", nullable = false, updatable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false, length = 20)
    private PaymentMethod paymentMethod;

    @OneToOne(mappedBy = "paymentCheck", fetch = FetchType.LAZY)
    @JsonIgnore
    private OneTimeDealEntity oneTimeInfo;

    @OneToOne(mappedBy = "paymentCheck", fetch = FetchType.LAZY)
    @JsonIgnore
    private SubscriptionDealEntity subscriptionInfo;

    @PrePersist
    private void setCheckNumber() {
        checkNumber = CheckNumberGeneratorUtil.generate();
    }
}
