package ua.shpp.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Table(name = "organization_access")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationAccessEntity {
    @Id
    private Long organizationId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @Column(nullable = false)
    private boolean accessAllowed = false;

    // TODO Якщо потрібно, можна додати поля типу "дата модерації", "хто підтвердив"
}
