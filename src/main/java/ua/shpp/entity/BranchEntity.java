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
@ToString
@Builder
@Table(name = "branches")
public class BranchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @Builder.Default
    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomEntity> rooms = new ArrayList<>();

    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServiceEntity> serviceEntities;

    @Builder.Default
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "branch_working_hours", joinColumns = @JoinColumn(name = "branch_id"))
    private List<WorkingHour> workingHours = new ArrayList<>();

    @OneToMany(mappedBy = "branch")
    private Set<EmployeeEntity> employees = new HashSet<>();

    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventTypeEntity> eventTypes = new ArrayList<>();

}