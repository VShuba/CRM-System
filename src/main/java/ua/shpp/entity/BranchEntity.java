package ua.shpp.entity;

import jakarta.persistence.*;
import lombok.*;
import ua.shpp.model.WorkingHour;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"organization", "rooms", "serviceEntities", "eventTypes", "employees", "workingHours"})
@Builder
@Table(name = "branches",
        indexes = {
                @Index(name = "idx_branch_org", columnList = "organization_id")
        })
public class BranchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column( length = 100)
    private String address;

    @Column(name = "phone_number", length = 100)
    private String phoneNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @Builder.Default
    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomEntity> rooms = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServiceEntity> serviceEntities = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "branch", fetch = FetchType.LAZY)
    private Set<EmployeeEntity> employees = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventTypeEntity> eventTypes = new ArrayList<>();

    @Builder.Default
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "branch_working_hours", joinColumns = @JoinColumn(name = "branch_id"))
    private List<WorkingHour> workingHours = new ArrayList<>();
}