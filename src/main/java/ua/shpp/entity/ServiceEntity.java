package ua.shpp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString(exclude = {"subscriptions", "oneTimeServices", "scheduleEvents", "employees"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "services",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"service_name", "branch_id"})
        },
        indexes = {
                @Index(name = "idx_service_branch", columnList = "branch_id"),
                @Index(name = "idx_service_name", columnList = "service_name")
        }
)
public class ServiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "service_name", nullable = false, length = 100)
    private String name;

    @Column(name = "service_color", nullable = false, length = 7)
    private String color;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private BranchEntity branch;

    @Builder.Default
    @OneToMany(
            mappedBy = "activity",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private List<OneTimeOfferEntity> oneTimeServices = new ArrayList<>();

    @Builder.Default
    @OneToMany(
            mappedBy = "serviceEntity",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private List<ScheduleEventEntity> scheduleEvents = new ArrayList<>();

    @Builder.Default
    @ManyToMany(mappedBy = "activities", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<SubscriptionOfferEntity> subscriptions = new ArrayList<>();

    @Builder.Default
    @ManyToMany(mappedBy = "services", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<EmployeeEntity> employees = new HashSet<>();

    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "service_room",
            joinColumns = @JoinColumn(name = "service_id"),
            inverseJoinColumns = @JoinColumn(name = "room_id")
    )
    private Set<RoomEntity> rooms = new HashSet<>(); // room

    @PreRemove
    private void preRemove() {
        // Delete from SubscriptionOfferEntity.activities
        for (SubscriptionOfferEntity sub : new ArrayList<>(subscriptions)) {
            sub.getActivities().remove(this);
        }

        // Delete from EmployeeEntity.services
        for (EmployeeEntity employee : new HashSet<>(employees)) {
            employee.getServices().remove(this);
        }

        // Delete from RoomEntity.services
        for (RoomEntity room : new HashSet<>(rooms)) {
            room.getServices().remove(this);
        }
    }
}