package ua.shpp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"branchEntities", "userOrganizations", "invitationEntities"})
@EqualsAndHashCode(exclude = {"branchEntities", "userOrganizations", "invitationEntities"})
@Entity
@Table(name = "organizations", indexes = {
        @Index(name = "idx_organization_name", columnList = "name")
})
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    @Builder.Default
    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<BranchEntity> branchEntities = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<UserOrganization> userOrganizations = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<InvitationEntity> invitationEntities = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScheduleEventEntity> scheduleEvent = new ArrayList<>();
}
