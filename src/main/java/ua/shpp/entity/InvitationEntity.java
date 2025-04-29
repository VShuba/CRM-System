package ua.shpp.entity;

import jakarta.persistence.*;
import lombok.*;
import ua.shpp.model.InvitationStatus;
import ua.shpp.model.OrgRole;
import ua.shpp.model.Role;

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

    @ManyToOne
    UserEntity creator;

    String recipientEmail;

    @Enumerated(EnumType.STRING)
    OrgRole role;

    LocalDateTime expiresAt;

    @ManyToOne
    Organization organization;

    @Enumerated(EnumType.STRING)
    InvitationStatus status;
}
