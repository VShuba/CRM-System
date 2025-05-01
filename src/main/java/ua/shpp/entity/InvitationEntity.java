package ua.shpp.entity;

import jakarta.persistence.*;
import lombok.*;
import ua.shpp.model.InvitationStatus;
import ua.shpp.model.OrgRole;

import java.time.LocalDateTime;

@Entity
@Table(name = "invitations")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class InvitationEntity {
    @Id
    String invitationId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "creator_id", nullable = false)
    UserEntity creator;

    @Column(name = "recipient_email", nullable = false, length = 100)
    String recipientEmail;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 30)
    OrgRole role;

    @Column(name = "expires_at", nullable = false)
    LocalDateTime expiresAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organization_id", nullable = false)
    Organization organization;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    InvitationStatus status;
}
