package ua.shpp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table
public class UserOrganization {

    @EmbeddedId
    private UserOrganizationId id = new UserOrganizationId();

    @ManyToOne
    @MapsId("userId") // @MapsId каже: "використовуй ID з @EmbeddedId"
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @MapsId("organizationId")
    @JoinColumn(name = "organization_id")
    private Organization organization;

    private LocalDate joinedAt; // Додаткове поле

}
