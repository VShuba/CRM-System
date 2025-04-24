package ua.shpp.entity.payment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
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

    @Column(name = "check_number", nullable = false, updatable = false)
    private String checkNumber;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "organization_name", nullable = false, updatable = false)
    private String organizationName;
    @Column(name = "branch_address", nullable = false, updatable = false)
    private String branchAddress;
    @Column(name = "branch_phone_number", nullable = false, updatable = false)
    private String branchPhoneNumber;

    @Column(name = "customer_name", nullable = false, updatable = false)
    private String customerName;
    @Column(name = "customer_phone_number", nullable = false, updatable = false)
    private String customerPhoneNumber;

    @Column(name = "offer_name", nullable = false, updatable = false)
    private String offerName;
    @Column(name = "price", nullable = false, updatable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false, length = 20)
    private PaymentMethod paymentMethod;

    @OneToOne(mappedBy = "paymentCheck", fetch = FetchType.LAZY)
    @JsonIgnore
    private OneTimeInfoEntity oneTimeInfo;

    @OneToOne(mappedBy = "paymentCheck", fetch = FetchType.LAZY)
    @JsonIgnore
    private SubscriptionInfoEntity subscriptionInfo;

    @PrePersist
    private void setCheckNumber(){
        checkNumber = CheckNumberGeneratorUtil.generate();
    }
}
