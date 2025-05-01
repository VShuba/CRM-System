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
@ToString(exclude = {"branchEntities", "userOrganizations"})
@EqualsAndHashCode(exclude = {"branchEntities", "userOrganizations"}) // needs for exclude ймовірну recursion
@Entity
@Table(name = "organizations")
public class Organization {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Builder.Default
    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // also needs for exclude recursion
    private List<BranchEntity> branchEntities = new ArrayList<>();

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<UserOrganization> userOrganizations;

    @OneToMany(mappedBy = "organization")
    private List<InvitationEntity> invitationEntities;
}
