package ua.shpp.entity;

import jakarta.persistence.*;
import lombok.*;
import ua.shpp.model.OrgRole;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user_organization",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "organization_id"})
        },
        indexes = {
                @Index(name = "idx_user_organization_user", columnList = "user_id"),
                @Index(name = "idx_user_organization_org", columnList = "organization_id")
        })
public class UserOrganization {

    @EmbeddedId
    private UserOrganizationId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("organizationId")
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @Column(name = "joined_at", nullable = false)
    private LocalDate joinedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "organization_role", nullable = false, length = 20)
    private OrgRole role;
}
